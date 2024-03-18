package com.example.tsd.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class FileUtil {

    /**
     * 校验文件是否合格
     *
     * @param file
     * @return
     */
    public static Boolean verifyFile(MultipartFile file) {
        if (file.getSize() == 0) {
            return false;
        }
        String originalFilename = file.getOriginalFilename();
        // 拿到拓展名
        String expandedName = originalFilename.substring(originalFilename.lastIndexOf('.'));
        if (expandedName == null) {
            return false;
        }
        return true;
    }

    /**
     * 将文件存在一个日期文件夹里面
     *
     * @param folder 文件夹的路径
     * @param file
     */
    public static String saveFile(String folder, MultipartFile file) throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 拿到文件名
        String originalFilename = file.getOriginalFilename();
        // 用时间作为文件夹名字
        String folderLastPath = simpleDateFormat.format(new Date());
        // 获取资源目录路径（绝对路径，到）
        String resourcePath = ResourceUtils.getURL("classpath:").getPath();
        System.out.println("classPath路径：" + resourcePath);

        // 拿到保存的全路径
        String folderPath = resourcePath + folder;
        System.out.println("保存的全路径：" + folderPath);

        // 判断文件夹是否存在
        File newFileFolder = new File(folderPath);
        if (!newFileFolder.exists()) {
            // 不存在就新建一个
            boolean mkdirs = newFileFolder.mkdirs();
            System.out.println("是否创建完成:" + mkdirs);
        }

        // 获取要保存的文件名全名称全路径
        String realPath = folderPath + "/" + originalFilename;
        System.out.println("要保存的文件名全名称全路：" + realPath);
        // 判断是否存在同名文件
        File originFile = new File(realPath);

        System.out.println("判断是否存在同名文件：" + originFile);
        System.out.println("判断是否存在同名文件：" + originFile.exists());

        // 存在就加个拓展名再返回
        if (originFile.exists()) {
            String fileName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
            String expandName = originalFilename.substring(originalFilename.lastIndexOf("."));
            fileName = fileName + "(1)";
            // 最后的文件名
            String newFileName = fileName + expandName;
            file.transferTo(new File(newFileFolder, newFileName));
            String realFileName = folderPath + newFileName;
        }

        // 获取文件对象
        File orgFile = new File(folderPath, originalFilename);
        file.transferTo(orgFile);


        System.out.println("上传后的路径");


        return realPath;
    }

    public List<File> getUploadDirectory() throws FileNotFoundException {
        File targetPath = new File(ResourceUtils.getURL("classpath:").getPath());
        System.out.println("文件保存路径:" + targetPath);
        if (!targetPath.exists()) {
            targetPath = new File("");
        }

        return null;
    }


    /**
     * 根据文件路径读取文件内容
     *
     * @param fileInPath
     * @throws IOException
     */
    public static void getFileContent(Object fileInPath) throws IOException {
        BufferedReader br = null;
        if (fileInPath == null) {
            return;
        }
        if (fileInPath instanceof String) {
            br = new BufferedReader(new FileReader(new File((String) fileInPath)));
        } else if (fileInPath instanceof InputStream) {
            br = new BufferedReader(new InputStreamReader((InputStream) fileInPath));
        }
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        br.close();
    }


    /**
     * 保存文件
     *
     * @param folder
     * @param file
     * @return
     */
    public static String saveToVClassPath(String folder, MultipartFile file) {
        try {
            // 获取资源目录路径（绝对路径，到）
            String resourcePath = ResourceUtils.getURL("classpath:").getPath();
            // 拿到保存的文件夹全路径
            String folderPath = resourcePath + folder;
            // 判断文件夹是否存在
            File newFileFolder = new File(folderPath);
            if (!newFileFolder.exists()) {
                // 不存在就新建一个
                boolean mkdirs = newFileFolder.mkdirs();
            }
            // 拿到文件名
            String originalFilename = file.getOriginalFilename();
            // 获取要保存的文件名全路径
            String realPath = folderPath + "/" + originalFilename;
            // 判断是否存在同名文件
            File originFile = new File(realPath);
            // 将文件名分割
            String fileName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
            String expandName = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFileName = null;
            if (originFile.exists()) {
                // 存在就+一个拓展名
                fileName = fileName + "(1)";
                // 最后的文件名
                newFileName = fileName + expandName;
            }
            // 保存文件
            File orgFile = null;
            if (newFileName == null) {
                orgFile = new File(folderPath, originalFilename);
                file.transferTo(orgFile);
                return "http://localhost:9999" + folder + originalFilename;
            } else {
                orgFile = new File(folderPath, newFileName);
                file.transferTo(orgFile);
                return "http://localhost:9999" + "/" + folder + "/" + newFileName;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * 保存文件分片
     * @param folder 保存到的文件夹
     * @param file 保存的文件分片
     * @param chunkIndex 分片偏移量
     * @param filename 文件名
     * @return
     */
    public static boolean saveChunkToVClassPath(String folder, MultipartFile file,String chunkIndex, String filename) {
        try {
            String left = filename.substring(0, filename.lastIndexOf("."));
            String right = filename.substring(filename.lastIndexOf("."));
            String fileName = left + "." + chunkIndex + right;

            // 获取资源目录路径（绝对路径，到）
            String resourcePath = ResourceUtils.getURL("classpath:").getPath();
            // 拿到保存的文件夹全路径
            String folderPath = resourcePath + folder;
            // 判断文件夹是否存在
            File newFileFolder = new File(folderPath);
            if (!newFileFolder.exists()) {
                // 不存在就新建一个
                boolean mkdirs = newFileFolder.mkdirs();
            }
            // 获取要保存的文件名全路径
            String realPath = folderPath + "/" + fileName;
            // 判断是否存在同名文件
            File originFile = new File(realPath);
            if (originFile.exists()) {
                // 同名文件说明传错了
                return false;
            }
            // 保存文件
            File orgFile = new File(folderPath, fileName);
            file.transferTo(orgFile);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 合并指定文件夹里面的某个名字的分片
     * @param folder
     * @param fileName
     * @return
     */
    public static synchronized boolean mergeChunkFile(String folder, String fileName) {
        try {
            // 获取资源目录路径（绝对路径，到）
            String resourcePath = ResourceUtils.getURL("classpath:").getPath();
            // 拿到保存的文件夹全路径
            String folderPath = resourcePath + folder;
            // 判断文件夹是否存在
            File newFileFolder = new File(folderPath);
            if (!newFileFolder.exists()) {
                // 不存在就报错
                return false;
            }
            // 获取分片文件列表
            File[] chunkFiles = new File(folderPath).listFiles();
            // 正则筛选
            String regex = fileName.substring(0, fileName.lastIndexOf(".")) + "\\.(\\d+)\\.";
            List<File> files = FileUtil.filterFiles(chunkFiles, fileName, regex);
            // 将 List 中的内容复制到 File[] 中
            File[] newChunkFiles = new File[files.size()];
            files.toArray(newChunkFiles);
            // 排序
            FileUtil.sortFiles("\\.(\\d+)\\.", newChunkFiles);
            // 要合并成为的文件
            String realFile = newFileFolder + "/" + fileName;
            File mergedFile = new File(realFile);


            // try (FileOutputStream fos = new FileOutputStream(mergedFile, true)) {
            //     for (File chunkFile : newChunkFiles) {
            //         System.out.println("合并的dile：" + chunkFile);
            //         byte[] buffer = new byte[8192];
            //         int bytesRead;
            //         try (FileInputStream fis = new FileInputStream(chunkFile)) {
            //             while ((bytesRead = fis.read(buffer)) != -1) {
            //                 fos.write(buffer, 0, bytesRead);
            //             }
            //         }
            //         chunkFile.delete(); // 删除已合并的分片文件
            //     }
            //     return true;
            // } catch (IOException e) {
            //     e.printStackTrace();
            //     return false;
            // }

            try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(mergedFile))) {
                for (File chunkFile : newChunkFiles) {
                    try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(chunkFile))) {
                        byte[] buffer = new byte[4096];
                        int bytesRead = 0;
                        while ((bytesRead = bis.read(buffer)) != -1) {
                            bos.write(buffer, 0, bytesRead);
                        }
                        bos.flush();
                    }
                    chunkFile.delete();
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 对一个文件列表排序
     *
     * @param patter 正则表达式
     * @param files  文件列表
     */
    public static void sortFiles(String patter, File[] files) {
        // 创建正则表达式模式,用于排序
        Pattern pattern = Pattern.compile(patter);
        Arrays.sort(files, (File o1, File o2) -> {
                    // 创建匹配器
                    Matcher matcher1 = pattern.matcher(o1.getName());
                    Matcher matcher2 = pattern.matcher(o2.getName());
                    if (matcher1.find() && matcher2.find()) {
                        // 提取匹配到的数字部分
                        String numberString1 = matcher1.group(1);
                        String numberString2 = matcher2.group(1);
                        // 将数字字符串解析为整数
                        int number1 = Integer.parseInt(numberString1);
                        int number2 = Integer.parseInt(numberString2);
                        return Integer.compare(number1, number2);
                    }
                    return o1.getName().compareTo(o2.getName());
                }
        );
    }


    /**
     * 对一个文件列表进行过滤
     *
     * @param files
     */
    public static List<File> filterFiles(File[] files, String fileName, String regex) {
        List<File> arrayList = new ArrayList<>();
        Pattern compile = Pattern.compile(regex);
        for (File file : files) {
            String name = file.getName();
            Matcher matcher = compile.matcher(name);
            if (matcher.find()) {
                arrayList.add(file);
            }
        }
        return arrayList;
    }
}
