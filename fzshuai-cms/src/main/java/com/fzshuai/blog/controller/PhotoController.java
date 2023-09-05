package com.fzshuai.blog.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaIgnore;
import com.fzshuai.blog.domain.bo.PhotoBO;
import com.fzshuai.blog.domain.dto.FrontPhotoDto;
import com.fzshuai.blog.domain.dto.PhotoDTO;
import com.fzshuai.blog.domain.dto.UpdateAlbumDto;
import com.fzshuai.blog.domain.vo.PhotoVO;
import com.fzshuai.blog.service.IPhotoService;
import com.fzshuai.common.annotation.Log;
import com.fzshuai.common.annotation.RepeatSubmit;
import com.fzshuai.common.core.controller.BaseController;
import com.fzshuai.common.core.domain.PageQuery;
import com.fzshuai.common.core.domain.R;
import com.fzshuai.common.core.page.TableDataInfo;
import com.fzshuai.common.core.validate.AddGroup;
import com.fzshuai.common.core.validate.EditGroup;
import com.fzshuai.common.enums.BusinessType;
import com.fzshuai.common.utils.poi.ExcelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 照片
 *
 * @author fzshuai
 * @date 2023-05-03
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/blog/photo")
public class PhotoController extends BaseController {

    private final IPhotoService photoService;

    /**
     * 前台根据相册id查看照片列表
     *
     * @param albumId 相册id
     * @return {@link R< FrontPhotoDto >} 照片列表
     */
    @SaIgnore
    @GetMapping("/albums/{albumId}/photos")
    public R<FrontPhotoDto> listPhotosByAlbumId(@PathVariable("albumId") Long albumId, PageQuery pageQuery) {
        return R.ok(photoService.listPhotosByAlbumId(albumId, pageQuery));
    }
    /**
     * 查询照片列表
     */
    @SaCheckPermission("blog:photo:list")
    @GetMapping("/list")
    public TableDataInfo<PhotoVO> list(PhotoBO bo, PageQuery pageQuery) {
        return photoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出照片列表
     */
    @SaCheckPermission("blog:photo:export")
    @Log(title = "照片", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(PhotoBO bo, HttpServletResponse response) {
        List<PhotoVO> list = photoService.queryList(bo);
        ExcelUtil.exportExcel(list, "照片", PhotoVO.class, response);
    }

    /**
     * 获取照片详细信息
     *
     * @param photoId 主键
     */
    @SaCheckPermission("blog:photo:query")
    @GetMapping("/{photoId}")
    public R<PhotoVO> getInfo(@NotNull(message = "主键不能为空")
                              @PathVariable Long photoId) {
        return R.ok(photoService.queryById(photoId));
    }

    /**
     * 新增照片
     */
    @SaCheckPermission("blog:photo:add")
    @Log(title = "照片", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PhotoDTO bo) {
        return toAjax(photoService.insertByBo(bo));
    }

    /**
     * 修改照片
     */
    @SaCheckPermission("blog:photo:edit")
    @Log(title = "照片", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody PhotoBO bo) {
        return toAjax(photoService.updateByBo(bo));
    }

    /**
     * 修改照片所属相册·
     */
    @SaCheckPermission("photo:photo:edit")
    @Log(title = "照片管理", businessType = BusinessType.UPDATE)
    @PutMapping("/album")
    public R<Void> editPhotoAlbum(@RequestBody UpdateAlbumDto updateAlbumDto) {
        return toAjax(photoService.updateByBo(updateAlbumDto));
    }
    /**
     * 删除照片
     *
     * @param photoIds 主键串
     */
    @SaCheckPermission("blog:photo:remove")
    @Log(title = "照片", businessType = BusinessType.DELETE)
    @DeleteMapping("/{photoIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] photoIds) {
        return toAjax(photoService.deleteWithValidByIds(Arrays.asList(photoIds), true));
    }
}
