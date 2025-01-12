package com.another.reportservice.service;


import com.another.reportservice.entity.Priority;
import com.another.reportservice.entity.Role;
import com.another.reportservice.entity.Status;
import com.another.reportservice.entity.reportEntity.PerformerEfficiencyReport;
import com.another.reportservice.entity.reportEntity.ProcessingTaskReportEntity;
import com.another.reportservice.entity.reportEntity.TaskPeriodReportEntity;
import com.another.reportservice.entity.reportEntity.UserPeriodReportEntity;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class ExcelReportService {

    @Value("${path.report}")
    private String path;

    // Форматируем даты/время, например: "yyyy.MM.dd" и "HH:mm"
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    public String createTaskProcessingReportExcel(List<ProcessingTaskReportEntity> tasks, String fileName) {

        // 1) Создаём "книгу" (Workbook)
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            // 2) Создаём лист (Sheet)
            Sheet sheet = workbook.createSheet("Task Report");

            // 3) Создаём "header" (первая строка)
            Row headerRow = sheet.createRow(0);
            // Заполним ячейки заголовка
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Тема");
            headerRow.createCell(2).setCellValue("Статус");
            headerRow.createCell(3).setCellValue("Исполнитель");
            headerRow.createCell(4).setCellValue("Дата создания");
            headerRow.createCell(5).setCellValue("Дата начала работы");
            headerRow.createCell(6).setCellValue("Дата завершения");
            headerRow.createCell(7).setCellValue("Время выполнения");

            // 4) Заполняем строки данными
            int rowIndex = 1; // со следующей строки (после заголовка)
            for (ProcessingTaskReportEntity task : tasks) {
                Row row = sheet.createRow(rowIndex++);
                // ID
                row.createCell(0).setCellValue(task.getTaskId() != null ? task.getTaskId() : 0);
                // Тема
                row.createCell(1).setCellValue(task.getTopic() != null ? task.getTopic() : "");
                // Статус
                row.createCell(2).setCellValue(task.getStatus() != null ? task.getStatus().name() : "");
                // Исполнитель
                row.createCell(3).setCellValue(task.getWorkerUsername() != null ? task.getWorkerUsername() : "");
                // Дата создания (разделяем на дату и время)
                if (task.getCreateDate() != null) {
                    String createDateStr = task.getCreateDate().format(DATE_FORMAT)
                            + " " + task.getCreateDate().format(TIME_FORMAT);
                    row.createCell(4).setCellValue(createDateStr);
                } else {
                    row.createCell(4).setCellValue("");
                }
                // Дата начала работы
                if (task.getOffToWorkTime() != null) {
                    String offToWorkTimeStr = task.getOffToWorkTime().format(DATE_FORMAT)
                            + " " + task.getOffToWorkTime().format(TIME_FORMAT);
                    row.createCell(5).setCellValue(offToWorkTimeStr);
                } else {
                    row.createCell(5).setCellValue("");
                }
                // Дата завершения
                if (task.getClosed() != null) {
                    String closedStr = task.getClosed().format(DATE_FORMAT)
                            + " " + task.getClosed().format(TIME_FORMAT);
                    row.createCell(6).setCellValue(closedStr);
                } else {
                    row.createCell(6).setCellValue("");
                }
                // Время выполнения (String, напр. "04:11")
                row.createCell(7).setCellValue(task.getCompletedTime() != null ? task.getCompletedTime() : "");
            }

            // Опционально: автоширина столбцов
            for (int i = 0; i < 8; i++) {
                sheet.autoSizeColumn(i);
            }

            // 5) Сохраняем книгу во временный поток
            workbook.write(baos);


            // Возвращаем файл собранный из массива байтов
            return bytesToFile(baos.toByteArray(), fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String createTaskPeriodReportExcel(TaskPeriodReportEntity reportEntity, String fileName) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Task Period Report");

            // Секция 1: Общая информация
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Общая информация");
            Row quantityRow = sheet.createRow(1);
            quantityRow.createCell(0).setCellValue("Количество задач");
            quantityRow.createCell(1).setCellValue(reportEntity.getQuantityTask());

            // Секция 2: Созданные задачи по месяцам
            int rowIndex = 3; // Пропускаем пару строк для отступа
            Row monthHeader = sheet.createRow(rowIndex++);
            monthHeader.createCell(0).setCellValue("Месяц");
            monthHeader.createCell(1).setCellValue("Количество задач");

            for (Map.Entry<Month, Long> entry : reportEntity.getCreateTaskByMonth().entrySet()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(entry.getKey().toString());
                row.createCell(1).setCellValue(entry.getValue());
            }

            // Секция 3: Созданные задачи по приоритетам
            rowIndex++;
            Row priorityHeader = sheet.createRow(rowIndex++);
            priorityHeader.createCell(0).setCellValue("Приоритет");
            priorityHeader.createCell(1).setCellValue("Количество задач");

            for (Map.Entry<Priority, Long> entry : reportEntity.getCreateTaskByPriority().entrySet()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(entry.getKey().toString());
                row.createCell(1).setCellValue(entry.getValue());
            }

            // Секция 4: Текущие задачи по статусу
            rowIndex++;
            Row statusHeader = sheet.createRow(rowIndex++);
            statusHeader.createCell(0).setCellValue("Статус");
            statusHeader.createCell(1).setCellValue("Количество задач");

            for (Map.Entry<Status, Long> entry : reportEntity.getGetNowTaskByStatus().entrySet()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(entry.getKey().toString());
                row.createCell(1).setCellValue(entry.getValue());
            }

            // Автоматическая ширина колонок
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);

            // Сохраняем книгу
            workbook.write(baos);

            return bytesToFile(baos.toByteArray(), fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String createUserPeriodReportExcel(UserPeriodReportEntity reportEntity, String fileName) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("User Period Report");

            // Секция 1: Общая информация
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Общая информация");
            Row quantityRow = sheet.createRow(1);
            quantityRow.createCell(0).setCellValue("Количество пользователей");
            quantityRow.createCell(1).setCellValue(reportEntity.getQuantityUsers());

            // Секция 2: Зарегистрированные пользователи по месяцам
            int rowIndex = 3; // Пропускаем пару строк для отступа
            Row monthHeader = sheet.createRow(rowIndex++);
            monthHeader.createCell(0).setCellValue("Месяц");
            monthHeader.createCell(1).setCellValue("Количество пользователей");

            for (Map.Entry<Month, Long> entry : reportEntity.getRegisterUserByMonth().entrySet()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(entry.getKey().toString());
                row.createCell(1).setCellValue(entry.getValue());
            }

            // Секция 3: Зарегистрированные пользователи по ролям
            rowIndex++;
            Row roleHeader = sheet.createRow(rowIndex++);
            roleHeader.createCell(0).setCellValue("Роль");
            roleHeader.createCell(1).setCellValue("Количество пользователей");

            for (Map.Entry<Role, Long> entry : reportEntity.getRegisterUserByRoleMonth().entrySet()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(entry.getKey().toString());
                row.createCell(1).setCellValue(entry.getValue());
            }

            // Автоматическая ширина колонок
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);

            // Сохраняем книгу
            workbook.write(baos);

            return bytesToFile(baos.toByteArray(), fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public String createPerformerEfficiencyReportExcel(PerformerEfficiencyReport report, String fileName) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Performer Efficiency Report");

            // Header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Успешно выполнено");
            headerRow.createCell(1).setCellValue("Задач нанято");
            headerRow.createCell(2).setCellValue("Среднее время выполнения");
            headerRow.createCell(3).setCellValue("Рейтинг пользователя");

            // Data row
            Row dataRow = sheet.createRow(1);
            dataRow.createCell(0).setCellValue(report.getSuccessfulImplementation() != null ? report.getSuccessfulImplementation() : 0);
            dataRow.createCell(1).setCellValue(report.getHiredTask() != null ? report.getHiredTask() : 0);
            dataRow.createCell(2).setCellValue(report.getAVGCompletionTime() != null ? report.getAVGCompletionTime() : 0.0);
            dataRow.createCell(3).setCellValue(report.getUserRating() != null ? report.getUserRating() : 0.0);

            for (int i = 0; i < 4; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(baos);
            return bytesToFile(baos.toByteArray(), fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String bytesToFile(byte[] data, String fileName) throws IOException {
        String pathToFile = path + fileName + ".xlsx";
        try (FileOutputStream fol = new FileOutputStream(pathToFile)) {
            fol.write(data);
        }
        return pathToFile;
    }
}
