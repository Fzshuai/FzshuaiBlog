package top.fzshuai.blog.service;

import top.fzshuai.blog.domain.dto.CategoryDto;
import top.fzshuai.blog.domain.vo.CategoryVo;
import top.fzshuai.blog.domain.bo.CategoryBo;
import top.fzshuai.blog.domain.vo.PageResultVo;
import top.fzshuai.common.core.page.TableDataInfo;
import top.fzshuai.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 文章分类Service接口
 *
 * @author fzshuai
 * @date 2023-05-03
 */
public interface ICategoryService {

    /**
     * 查询前台分类列表
     *
     * @return 分类列表
     */
    PageResultVo<CategoryDto> queryCategoryList();

    /**
     * 查询文章分类
     */
    CategoryVo queryCategoryById(Long categoryId);

    /**
     * 查询文章分类列表
     */
    TableDataInfo<CategoryVo> queryCategoryList(CategoryBo bo, PageQuery pageQuery);

    /**
     * 查询文章分类列表
     */
    List<CategoryVo> queryCategoryList(CategoryBo bo);

    /**
     * 新增文章分类
     */
    Boolean insertByBo(CategoryBo bo);

    /**
     * 修改文章分类
     */
    Boolean updateByBo(CategoryBo bo);

    /**
     * 校验并批量删除文章分类信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
