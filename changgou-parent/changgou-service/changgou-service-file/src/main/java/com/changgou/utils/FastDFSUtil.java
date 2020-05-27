package com.changgou.utils;

import com.changgou.file.FastDFSFile;
import lombok.extern.slf4j.Slf4j;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 实现 FastDFS 文件管理
 */
@Slf4j
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
     *
     * @return
     * @throws Exception
     */
    public static TrackerServer getTrackerServer() throws Exception {
        // 创建一个 Tracker 访问的客户端对象 TrackerClient
        TrackerClient trackerClient = new TrackerClient();
        // 通过 TrackerClient 访问 TrackerServer 服务，获取连接信息
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerServer;
    }

    public static StorageClient getStorageClient(TrackerServer trackerServer) {
        return new StorageClient(trackerServer, null);
    }

    /**
     * 文件上传
     * @param fastDFSFile 上传的文件信息封装
     * @return
     */
    public static String[] upload(FastDFSFile fastDFSFile) throws Exception{
        // 通过 TrackerClient 访问 TrackerServer 服务，获取连接信息
        TrackerServer trackerServer = getTrackerServer();
        // 通过 TrackerServer 的连接信息可以获取 Storage 的连接信息，创建 StorageClient 对象存储 Storage 的连接信息
        StorageClient storageClient = getStorageClient(trackerServer);
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
        String[] uploads = storageClient.upload_file(fastDFSFile.getContent(), fastDFSFile.getExt(), meta_list);
        return uploads;
    }

    public static FileInfo getFileInfo(String groupName, String remoteFileName) throws Exception {
        // 通过 TrackerClient 访问 TrackerServer 服务，获取连接信息
        TrackerServer trackerServer = getTrackerServer();
        StorageClient storageClient = getStorageClient(trackerServer);
        return storageClient.get_file_info(groupName, remoteFileName);
    }

    /**
     * 文件下载
     * @param groupName
     * @param remoteFileName
     * @return
     * @throws Exception
     */
    public static InputStream downFile(String groupName, String remoteFileName) throws Exception {
        // 通过 TrackerClient 访问 TrackerServer 服务，获取连接信息
        TrackerServer trackerServer = getTrackerServer();
        StorageClient storageClient = getStorageClient(trackerServer);

        byte[] buffer = storageClient.download_file(groupName, remoteFileName);
        return new ByteArrayInputStream(buffer);
    }

    /**
     * 删除文件
     * @param groupName
     * @param remoteFileName
     * @return
     * @throws Exception
     */
    public static void deleteFile(String groupName, String remoteFileName) throws Exception {
        // 通过 TrackerClient 访问 TrackerServer 服务，获取连接信息
        TrackerServer trackerServer = getTrackerServer();
        StorageClient storageClient = getStorageClient(trackerServer);

        storageClient.delete_file(groupName, remoteFileName);
    }

    /**
     * 获取 storage 的信息
     * @return
     * @throws Exception
     */
    public static StorageServer getStorage() throws Exception {
        // 创建一个 TrackerClient 对象，通过 TrackerClient 对象访问 TrackerServer
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageClient storageClient = getStorageClient(trackerServer);
        return trackerClient.getStoreStorage(trackerServer);
    }

    /**
     * 获取 storage 数组的信息
     * @param groupName
     * @param remoteFileName
     * @return
     * @throws Exception
     */
    public static ServerInfo[] getServerInfo(String groupName, String remoteFileName) throws Exception {
        // 创建一个 TrackerClient 对象，通过 TrackerClient 对象访问 TrackerServer
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();

        return trackerClient.getFetchStorages(trackerServer, groupName, remoteFileName);
    }

    public static String getTrackerInfo() throws Exception {
        TrackerServer trackerServer = getTrackerServer();
        String ip = trackerServer.getInetSocketAddress().getHostString();
        int tracker_http_port = ClientGlobal.getG_tracker_http_port();
        return "http://" + ip + ":" + tracker_http_port;
    }

    public static void main(String[] args) throws Exception {
//        // 文件上传
//        FileInfo fileInfo = getFileInfo("group1", "M00/00/00/wKgvjl7NvxmAJIzHAAGfCM0GYss770.jpg");
//        if (fileInfo == null) {
//            log.info("文件不存在");
//            return ;
//        }
//        log.info("ip = " + fileInfo.getSourceIpAddr());
//        log.info("size = " + fileInfo.getFileSize());

//        // 文件下载
//        InputStream in = downFile("group1", "M00/00/00/wKgvjl7NvxmAJIzHAAGfCM0GYss770.jpg");
//        // 将文件写入本地磁盘
//        FileOutputStream os = new FileOutputStream("E:/study/project/backupFile/1.jpg");
//        // 定义一个缓冲区
//        byte[] buffer = new byte[1024];
//        while (in.read(buffer) != -1) {
//            os.write(buffer);
//        }
//        os.flush();
//        os.close();
//        in.close();

//        // 删除文件
//        deleteFile("group1", "M00/00/00/wKgvjl7NwZqAYOkMAAGfCM0GYss913.jpg");
//        log.info("删除成功");
//        //  获取 storage 信息
//        StorageServer storageServer = getStorage();
//        log.info("pathIndex = " + storageServer.getStorePathIndex());
//        log.info("ip = " + storageServer.getInetSocketAddress().getAddress());
//        log.info("host = " + storageServer.getInetSocketAddress().getHostName());
//        log.info("port = " + storageServer.getInetSocketAddress().getPort());

//        // 获取 storage 组的IP和端口信息
//        ServerInfo[] groups = getServerInfo("group1", "/M00/00/00/wKgvjl7Nz_yAVmr9AAGfCM0GYss556.jpg");
//        for (ServerInfo server : groups) {
//            log.info("addr = " + server.getIpAddr());
//            log.info("port = " + server.getPort());
//        }
        log.info(getTrackerInfo());
    }
}
