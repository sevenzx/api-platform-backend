package com.xuan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuan.model.entity.InvokeInterface;
import org.apache.ibatis.annotations.Param;

/**
 * @author xuan
 * @description 针对表【invoke_interface(用户调用接口关系表)】的数据库操作Mapper
 * @createDate 2023-03-30 15:31:14
 * @Entity generator.domain.InvokeInterface
 */
public interface InvokeInterfaceMapper extends BaseMapper<InvokeInterface> {
	/**
	 * 根据用户id、path、method查询
	 *
	 * @param userId 用户id
	 * @param path   接口路径
	 * @param method 接口请求方法
	 * @return InvokeInterface
	 */
	InvokeInterface selectByUserIdPathAndMethod(@Param("userId") Long userId, @Param("path") String path, @Param("method") String method);
}




