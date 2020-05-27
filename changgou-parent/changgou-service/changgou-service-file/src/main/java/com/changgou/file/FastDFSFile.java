package com.changgou.file;

import lombok.Data;

import java.io.Serializable;

/**
 * 封装文件上传信息
 */
@Data
public class FastDFSFile implements Serializable {
    private static final long serialVersionUID = 1L;

    // 文件名
    private String name;
    // 文件内容
    private byte[] content;
    // 文件扩展名
    private String ext;
    // 文件MD5摘要值
    private String md5;
    // 文件创建作者
    private String author;

    public FastDFSFile(String name, byte[] content, String ext) {
        this.name = name;
        this.content = content;
        this.ext = ext;
    }

    public FastDFSFile(String name, byte[] content, String ext, String md5, String author) {
        this.name = name;
        this.content = content;
        this.ext = ext;
        this.md5 = md5;
        this.author = author;
    }
}
