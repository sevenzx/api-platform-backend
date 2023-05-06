package com.xuan.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuan.common.ErrorCode;
import com.xuan.common.FuzzyQueryRequest;
import com.xuan.common.PageRequest;
import com.xuan.exception.BusinessException;
import com.xuan.mapper.UserMapper;
import com.xuan.model.entity.User;
import com.xuan.model.vo.PageVO;
import com.xuan.model.vo.UserVO;
import com.xuan.service.CommonService;
import com.xuan.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.xuan.constant.CommonConstant.USER_LOGIN_STATE;

/**
 * @author: xuan
 * @since: 2023/2/10
 */

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
		implements UserService {

	/**
	 * 盐值，混淆密码
	 */
	private static final String SALT = "xuan";

	/**
	 * 用户默认头像
	 */
	private static final String USER_DEFAULT_AVATAR = "https://s2.loli.net/2023/04/26/IS185lD9XzvMfwq.png";

	/**
	 * 用户账户正则
	 */
	private static final String USER_ACCOUNT_REGEX = "^[a-zA-Z][a-zA-Z0-9_]{3,15}$";

	/**
	 * 用户密码正则
	 */
	private static final String USER_PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9])(.{8,16})$";

	/**
	 * 随机生成用户名前缀
	 */
	private static final String USERNAME_PREFIX = "user_";

	/**
	 * 随机生成用户名后缀长度
	 */
	private static final int USER_NAME_SUFFIX_LENGTH = 5;


	@Override
	public long userRegister(String userAccount, String userPassword, String checkPassword, HttpServletRequest request) {
		// 1.账户、密码校验
		// 是否为空
		if (StrUtil.hasBlank(userAccount, userPassword, checkPassword)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数有空值");
		}
		// 账户
		if (!ReUtil.contains(USER_ACCOUNT_REGEX, userAccount)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户名不符合要求");
		}
		// 密码
		if (!ReUtil.contains(USER_PASSWORD_REGEX, userPassword)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码不符合要求");
		}
		// 两次输入密码是否一致
		if (!userPassword.equals(checkPassword)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入密码不一致");
		}
		// 查询数据库是否能够注册账户
		long count = this.count(new LambdaQueryWrapper<User>().eq(User::getUserAccount, userAccount));
		if (count > 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名重复");
		}

		// 2.加密
		String encryptPassword = DigestUtil.md5Hex(SALT + userPassword);

		// 3.插入数据库
		User user = new User();
		user.setUserAccount(userAccount);
		user.setUserPassword(encryptPassword);
		// 随机生成用户名
		String username = USERNAME_PREFIX + RandomUtil.randomString(USER_NAME_SUFFIX_LENGTH);
		user.setUsername(username);
		// 设置默认头像
		user.setUserAvatar(USER_DEFAULT_AVATAR);
		// 生成key secret
		String userKey = "cli_" + DigestUtil.md5Hex(SALT + userAccount + RandomUtil.randomNumbers(4));
		String userSecret = DigestUtil.md5Hex(SALT + userAccount + RandomUtil.randomNumbers(8));
		user.setUserKey(userKey);
		user.setUserSecret(userSecret);
		boolean saveResult = this.save(user);
		if (!saveResult) {
			log.info("user register failed,please check account,password and checkPassword");
			throw new BusinessException(ErrorCode.SYSTEM_ERROR);
		}

		// 注册成功后写入session直接登录
		UserVO userVO = new UserVO();
		BeanUtil.copyProperties(user, userVO);
		request.getSession().setAttribute(USER_LOGIN_STATE, userVO);
		return user.getId();
	}

	@Override
	public UserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
		// 1.账户、密码校验
		// 是否为空
		if (StrUtil.hasBlank(userAccount, userPassword)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数有空值");
		}
		// 账户
		if (!ReUtil.contains(USER_ACCOUNT_REGEX, userAccount)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户名不符合要求");
		}
		// 密码长度
		if (!ReUtil.contains(USER_PASSWORD_REGEX, userPassword)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码不符合要求");
		}

		// 2.加密
		String encryptPassword = DigestUtil.md5Hex(SALT + userPassword);
		LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
		User originUser = this.getOne(queryWrapper
				.eq(User::getUserAccount, userAccount)
				.eq(User::getUserPassword, encryptPassword)
		);

		if (originUser == null) {
			log.info("user login failed,please check whether the account and password match");
			throw new BusinessException(ErrorCode.OPERATION_ERROR, "login failed");
		}

		// 3.信息脱敏
		UserVO userVO = BeanUtil.copyProperties(originUser, UserVO.class);
		// 4.session设置登录态
		request.getSession().setAttribute(USER_LOGIN_STATE, userVO);
		return userVO;
	}

	@Override
	public UserVO currentUser(HttpServletRequest request) {
		Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
		if (userObj == null) {
			return null;
		}
		UserVO user = (UserVO) userObj;
		// 之前的信息可能被更新了 所以需要再次获取信息
		Long id = user.getId();
		// 除了查库也可以用缓存
		User currentUser = this.getById(id);
		if (currentUser == null) {
			throw new BusinessException(ErrorCode.SYSTEM_ERROR);
		}
		// TODO 判断用户是否合法
		UserVO userVO = BeanUtil.copyProperties(currentUser, UserVO.class);
		request.getSession().setAttribute(USER_LOGIN_STATE, userVO);
		return userVO;
	}

	@Override
	public boolean userLogout(HttpServletRequest request) {
		if (request.getSession().getAttribute(USER_LOGIN_STATE) == null) {
			throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
		}
		// 移除session里的用户信息
		request.getSession().removeAttribute(USER_LOGIN_STATE);
		return true;
	}

	@Override
	public PageVO<UserVO> listUserByPage(PageRequest pageRequest) {

		Page<User> userPage = CommonService.listByPage(this, pageRequest);

		// 信息脱敏、封装数据
		return getListUserVoByPage(userPage);
	}

	@Override
	public PageVO<UserVO> listUserByFuzzy(FuzzyQueryRequest fuzzyQueryRequest) {

		Page<User> queryPage = CommonService.listByFuzzy(this, fuzzyQueryRequest);

		// 信息脱敏、封装数据
		return getListUserVoByPage(queryPage);
	}

	@Override
	public Map<Long, UserVO> listUserByIds(List<Long> ids) {
		if (CollUtil.isEmpty(ids)) {
			return null;
		}
		List<User> userList = this.listByIds(ids);
		return userList.stream()
				.collect(Collectors.toMap(User::getId, user -> {
					UserVO userVO = new UserVO();
					BeanUtils.copyProperties(user, userVO);
					return userVO;
				}));
	}

	/**
	 * 用户信息脱敏、封装数据
	 *
	 * @param queryPage 分页查询结果
	 * @return PageVO<UserVO>
	 */
	private PageVO<UserVO> getListUserVoByPage(Page<User> queryPage) {
		List<UserVO> userVOList = queryPage.getRecords().stream().map(user -> {
			UserVO userVO = new UserVO();
			BeanUtils.copyProperties(user, userVO);
			return userVO;
		}).collect(Collectors.toList());

		return new PageVO<>(userVOList, queryPage.getCurrent(), queryPage.getSize(), queryPage.getTotal());
	}
}




