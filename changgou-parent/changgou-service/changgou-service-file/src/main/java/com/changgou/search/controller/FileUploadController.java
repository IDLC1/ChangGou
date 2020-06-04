package com.changgou.search.controller;

import com.changgou.common.entity.Result;
import com.changgou.common.entity.StatusCode;
import com.changgou.file.FastDFSFile;
import com.changgou.utils.FastDFSUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
@CrossOrigin
public class FileUploadController {

    @PostMapping
    public Result upload(@RequestParam(value = "file")MultipartFile file) throws Exception {
        FastDFSFile fastDFSFile = new FastDFSFile(
                file.getOriginalFilename(),
                file.getBytes(),
                StringUtils.getFilenameExtension(file.getOriginalFilename())
        );
        // 调用 FastDFSUtil 工具类将文件传入到 FastDFS 中
        String[] uploads = FastDFSUtil.upload(fastDFSFile);
        String url = FastDFSUtil.getTrackerInfo() + "/" + uploads[0] + "/" + uploads[1];
        return new Result(true, StatusCode.OK, "上传成功", url);
    }
}
