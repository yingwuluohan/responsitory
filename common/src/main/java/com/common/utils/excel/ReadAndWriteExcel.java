package com.common.utils.excel;

import com.common.utils.modle.User;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by   on 2017/2/10.
 */
public class ReadAndWriteExcel {
    private Logger log = Logger.getLogger(this.getClass());

    public List<User> readUserXls(MultipartFile file, String userName   ) throws IOException {
        List<User> userList = new LinkedList<User>();
        Workbook wb = null;
        int line = 0;//异常行数
        try {
            //wb = WorkbookFactory.create(ins);

            wb = WorkbookFactory.create(file.getInputStream());
            //3.得到Excel工作表对象
            Sheet sheet = wb.getSheetAt(0);
            //总行数
            int trLength = sheet.getLastRowNum();
            User user = null;
            for (int i = 1; i <= trLength; i++) {
                try{
                    int num =0;
                    line = i;
                    int hasErrorNum = 0;
                    user = new User();
                    //得到Excel工作表的行
                    Row row = sheet.getRow(i);
                    //得到Excel工作表指定行的单元格
                    Cell realNameCell = row.getCell(0);
                    Cell typeCell = row.getCell(1);
                    Cell endTimeCell = row.getCell(2);
                    Cell hasPowerYouke = row.getCell(3);//课堂权限状态标示
                    Cell rangeCell = row.getCell(4);
                    Cell mobileCell = row.getCell(5);
                    Cell emailCell = row.getCell(6);
                }catch( Exception e2 ){}
            }
        }catch ( Exception e) {
            e.printStackTrace();
        }finally{
            if( null != wb )
                wb.close();
        }
        return userList;
    }


    /**
     * 生成excel
     */
    public void makeRightExcel(List<User> userList , HSSFSheet sheet ,
                               HSSFRow row  , HSSFCellStyle style ) {
        {
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
            HSSFCell cell = row.createCell((short) 0);
            //cell.setCellValue("账号");
            //cell.setCellStyle(style);
            //cell = row.createCell((short) 1);
            cell.setCellValue("姓名");
            cell.setCellStyle(style);
            cell = row.createCell((short) 1);
            cell.setCellValue("身份");
            cell.setCellStyle(style);
            cell = row.createCell((short) 2);
            cell.setCellValue("账号截止日期");
            cell.setCellStyle(style);
            cell = row.createCell((short) 3);
            cell.setCellValue("学段");
            cell.setCellStyle(style);
            cell = row.createCell((short)4);
            cell.setCellValue("手机号");
            cell.setCellStyle(style);
            cell = row.createCell((short) 5);
            cell.setCellValue("邮箱");
            cell.setCellStyle(style);
            cell = row.createCell((short) 6);
            cell.setCellValue("结果");
            cell.setCellStyle(style);
            // 第五步，写入实体数据 实际应用中这些数据从数据库得到，
            for (int i = 0; i < userList.size(); i++) {
                row = sheet.createRow((int) i + 1);
                User user = (User) userList.get(i);
                // 第四步，创建单元格，并设置值
                // row.createCell((short) 0).setCellValue( user.getUserName() );
                row.createCell((short) 0).setCellValue( user.getRealName());
                if( 1 == user.getType() ){
                    row.createCell((short) 1).setCellValue( "老师" );
                }else{
                    row.createCell((short) 1).setCellValue( "学生" );
                }
                row.createCell((short) 2).setCellValue( "" );
                row.createCell((short) 3).setCellValue( "" );
                row.createCell((short) 4).setCellValue( user.getMobile() );
                row.createCell((short) 5).setCellValue( user.getEmail() );
                row.createCell((short) 6).setCellValue( user.getUserId()  );
            }
        }
    }
}
