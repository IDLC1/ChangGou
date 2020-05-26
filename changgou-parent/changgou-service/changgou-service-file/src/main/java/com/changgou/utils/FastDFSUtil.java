package com.changgou.utils;

import com.changgou.file.FastDFSFile;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.core.io.ClassPathResource;

import javax.sound.midi.Track;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 实现 FastDFS 文件管理
 */
public class FastDFSUtil {

    /**
     * 加载 Tracker 连接信息
     */
    static {
        try {
            // 查找 classpath 下的文件路径
            String filename = new ClassPathResource("fdfs_client.conf").getPath();
            // 加载 Tracker 链接信息
            ClientGlobal.init(filename);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件上传
     * @param fastDFSFile 上传的文件信息封装
     */
    public static void upload(FastDFSFile fastDFSFile) throws Exception{
        // 创建一个 Tracker 访问的客户端对象 TrackerClient
        TrackerClient trackerClient = new TrackerClient();
        // 通过 TrackerClient 访问 TrackerServer 服务，获取连接信息
        TrackerServer trackerServer = trackerClient.getConnection();
        // 通过 TrackerServer 的连接信息可以获取 Storage 的连接信息，创建 StorageClient 对象存储 Storage 的连接信息
        StorageClient storageClient = new StorageClient(trackerServer, null);
        //
        /**
         * 通过 StorageClient 访问 Storage,实现文件上传，并获取文件上传后的存储信息
         * 1. 上传文件的字节数组
         * 2. 文件的扩展名
         * 3. 附加参数，如地址、时间等
         */
        // 附加参数
        NameValuePair[] meta_list = new NameValuePair[3];
        meta_list[0] = new NameValuePair("地址", "北京");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        meta_list[1] = new NameValuePair("时间", sdf.format(date));
        meta_list[2] = new NameValuePair("作者", fastDFSFile.getAuthor());
        storageClient.upload_file(fastDFSFile.getContent(), fastDFSFile.getExt(), meta_list);
    }

}
