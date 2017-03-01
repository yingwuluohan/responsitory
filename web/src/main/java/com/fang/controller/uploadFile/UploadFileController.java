package com.fang.controller.uploadFile;

import com.modle.User;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by fn on 2017/3/1.
 */
@RequestMapping("/upload")
@Controller
public class UploadFileController {
    Logger log = Logger.getLogger( this.getClass());

    @RequestMapping( value="/uploadExcel" ,method= RequestMethod.POST )
    public void uploadExcel(MultipartFile file , HttpServletRequest request , HttpServletResponse response ) {
        log.info("大小:");
        Workbook wb = null;
        int line = 0;//异常行数
        try {
            wb = WorkbookFactory.create(file.getInputStream());
            //3.得到Excel工作表对象
            Sheet sheet = wb.getSheetAt(0);
            //总行数
            int trLength = sheet.getLastRowNum();
            for (int i = 1; i <= trLength; i++) {
                try {
                    //得到Excel工作表的行
                    Row row = sheet.getRow(i);
                    //得到Excel工作表指定行的单元格
                    Cell realNameCell = row.getCell(0);
                } catch (Exception e) {
                }
            }
        }catch ( Exception e1 ){}
        finally{
            if( null != wb )
                try {
                    wb.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
