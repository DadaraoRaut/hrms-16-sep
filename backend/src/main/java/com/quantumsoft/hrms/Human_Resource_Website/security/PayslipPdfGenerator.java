package com.quantumsoft.hrms.Human_Resource_Website.security;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.quantumsoft.hrms.Human_Resource_Website.entity.Payroll;
import com.quantumsoft.hrms.Human_Resource_Website.entity.SalaryStructure;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

public class PayslipPdfGenerator {

    public static ByteArrayInputStream generatePayslip(Payroll payroll, SalaryStructure structure) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        // Company header with logo
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{2, 5});

        // Fonts
        Font headerFont = new Font(Font.HELVETICA, 18, Font.BOLD, Color.getHSBColor(0.567F, 0.613F, 0.443F));
        Font subHeaderFont = new Font(Font.HELVETICA, 11, Font.NORMAL);
        Font tableFont = new Font(Font.HELVETICA, 10, Font.NORMAL);
        Font boldFont = new Font(Font.HELVETICA, 10, Font.BOLD);

        // Load logo image
        try {
            Image logo = Image.getInstance("src/main/resources/static/images/logo.png");
            logo.scaleToFit(150, 100);
            PdfPCell logoCell = new PdfPCell(logo, false);
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // this makes it vertically centered
            logoCell.setPaddingBottom(5);
            headerTable.addCell(logoCell);
        } catch (Exception e) {
            // If logo not found, add empty cell
            PdfPCell noLogoCell = new PdfPCell(new Phrase(""));
            noLogoCell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(noLogoCell);
        }


        // Company Name & Address cell
        PdfPCell companyCell = new PdfPCell();
        companyCell.setBorder(Rectangle.NO_BORDER);
        companyCell.setVerticalAlignment(Element.ALIGN_MIDDLE);  // vertically center the text with logo
        companyCell.setPaddingLeft(5);

        Paragraph company = new Paragraph("QuantumSoft Technologies Private Limited", headerFont);
        company.setAlignment(Element.ALIGN_CENTER);
        companyCell.addElement(company);

        Paragraph addressPara = new Paragraph("Silver Oak Plaza, Near Podar International School, Pote-Patil Road,\nAmravati, Maharashtra", subHeaderFont);
        addressPara.setAlignment(Element.ALIGN_CENTER);
        companyCell.addElement(addressPara);

        headerTable.addCell(companyCell);

        // Add headerTable to document
        document.add(headerTable);

//        // Add spacing after header
//        document.add(new Paragraph(" "));

        //Payslip Title
        Paragraph payslipTitle = new Paragraph("Payslip for the month of " + payroll.getMonth()+"-"+payroll.getYear()+"\n", new Font(Font.HELVETICA, 13, Font.BOLD));
        payslipTitle.setAlignment(Element.ALIGN_CENTER);
        document.add(payslipTitle);
        document.add(new Paragraph(" "));


        // Horizontal line separator
        LineSeparator ls = new LineSeparator();
        ls.setLineColor(Color.BLACK);
        ls.setLineWidth(1f);
        document.add(ls);

        // Employee details table
        PdfPTable empTable = new PdfPTable(4);
        empTable.setWidthPercentage(100);
        empTable.setWidths(new float[]{2, 3, 2, 3});

        addCell(empTable, "Employee Code", boldFont);
        addCell(empTable, payroll.getEmployee().getEmployeeCode(), tableFont);


        addCell(empTable, "Employee Name", boldFont);
        addCell(empTable, payroll.getEmployee().getFirstName() + " " + payroll.getEmployee().getLastName(), tableFont);

        addCell(empTable, "Employee No", boldFont);
        addCell(empTable, payroll.getEmployee().getEmpId().toString(), tableFont);

        addCell(empTable, "Designation", boldFont);
        addCell(empTable, payroll.getEmployee().getDesignation(), tableFont);

        addCell(empTable, "Joining Date", boldFont);
        addCell(empTable, payroll.getEmployee().getJoiningDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), tableFont);

        addCell(empTable, "Location", boldFont);
        addCell(empTable, payroll.getEmployee().getLocation(), tableFont);

        addCell(empTable, "Department", boldFont);
        addCell(empTable, payroll.getEmployee().getDepartment().getName(), tableFont);

        addCell(empTable, "Bank Name", boldFont);
        addCell(empTable, payroll.getEmployee().getBankDetail().getBankName(), tableFont);

        addCell(empTable, "Bank Account No.", boldFont);
        addCell(empTable, payroll.getEmployee().getBankDetail().getAccountNumber(), tableFont);

        addCell(empTable, "PAN", boldFont);
        addCell(empTable, "-", tableFont);

        addCell(empTable, "UAN", boldFont);
        addCell(empTable, "-", tableFont);

        addCell(empTable, "PF No.", boldFont);
        addCell(empTable, "-", tableFont);

        document.add(empTable);
        document.add(new Paragraph(" "));

// Attendance Summary Title
        Paragraph summaryTitle = new Paragraph("Attendance Summary", boldFont);
        summaryTitle.setAlignment(Element.ALIGN_CENTER);
        summaryTitle.setSpacingBefore(10f);
        document.add(summaryTitle);

// Attendance Summary Table with multiple columns in a single row
        PdfPTable summaryTable = new PdfPTable(5);  // 5 columns (or however many you need)
        summaryTable.setWidthPercentage(100);
        summaryTable.setSpacingBefore(10f);
        summaryTable.setWidths(new float[]{3, 3, 3, 3, 3});

// Add Header cells
        addHeaderCell(summaryTable, "Total Working Days:"+f(payroll.getWorkingDays()), boldFont);
        addHeaderCell(summaryTable, "Payable Days:"+f(payroll.getPayableDays()), boldFont);
        addHeaderCell(summaryTable, "LOP Days:"+f(payroll.getPaidLeaveDays()), boldFont);
        addHeaderCell(summaryTable, "Present Days:"+f(payroll.getPresentDays()), boldFont);
        addHeaderCell(summaryTable, "Arrear Days:"+"0", boldFont);

// Add summaryTable to document
        document.add(summaryTable);
        document.add(new Paragraph(" "));

        // Earnings/Deductions table
        PdfPTable salaryTable = new PdfPTable(7);
        salaryTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        salaryTable.setWidthPercentage(100);
        salaryTable.setWidths(new float[]{5, 3, 3, 3, 3,5,3});

        addHeaderCell(salaryTable, "EARNINGS", boldFont);
        addHeaderCell(salaryTable, "MONTHLY RATE", boldFont);
        addHeaderCell(salaryTable, "CURRENT MONTH", boldFont);
        addHeaderCell(salaryTable, "ARREAR", boldFont);
        addHeaderCell(salaryTable, "TOTAL", boldFont);
        addHeaderCell(salaryTable, "DEDUCTION", boldFont);
        addHeaderCell(salaryTable, "TOTAL", boldFont);


        addDataRow(salaryTable, "Basic Salary", f(structure.getBasicSalary()), f(structure.getBasicSalary()), "0.00", f(structure.getBasicSalary()), "PF",f(structure.getPfDeduction()), tableFont);
        addDataRow(salaryTable, "House Rent Allowance", f(structure.getHra()), f(structure.getHra()), "0.00", f(structure.getHra()),"PROF TAX",f(structure.getTaxDeduction()), tableFont);
        addDataRow(salaryTable, "Other Allowance", f(structure.getSpecialAllowance()), f(structure.getSpecialAllowance()), "0.00", f(structure.getSpecialAllowance()),"","",tableFont);
        addDataRow(salaryTable, "bonus", f(structure.getBonus()), f(structure.getBonus()), "0.00", f(structure.getBonus()),"","",tableFont);

//        addDataRow(salaryTable, "Gross Earnings", "", "", "", f(payroll.getTotalEarnings()));
//        addDataRow(salaryTable,"TOTAL DEDUCTIONS",f(payroll.getTotalDeductions()));

        addHeaderCell(salaryTable, "GROSS EARNING", boldFont);
        addHeaderCell(salaryTable, "", boldFont);
        addHeaderCell(salaryTable, "", boldFont);
        addHeaderCell(salaryTable, "", boldFont);
        addHeaderCell(salaryTable,f(payroll.getTotalEarnings()), boldFont);
        addHeaderCell(salaryTable, "TOTAL DEDUCTIONS", boldFont);
        addHeaderCell(salaryTable, f(payroll.getTotalDeductions()), boldFont);

        document.add(salaryTable);
        document.add(new Paragraph(" "));

        // Net Pay
        DecimalFormat df = new DecimalFormat("#,##0.00");
        Paragraph netPay = new Paragraph("NET PAY: " + df.format(payroll.getNetSalary())+"\n (Rupees " + convertAmountToWords((int) payroll.getNetSalary()) + " Only)", boldFont);
        netPay.setSpacingBefore(10);
        document.add(netPay);

        // Tax Computation Summary
        document.add(new Paragraph("\nSUMMARY OF TAX COMPUTATION AS PER NEW REGIME", boldFont));

        PdfPTable taxTable = new PdfPTable(2);
        taxTable.setWidthPercentage(60);
        taxTable.setSpacingBefore(5f);

        addCell(taxTable, "Gross Salary (Excl. Reimbursement)", tableFont);
        addCell(taxTable, f(payroll.getTotalEarnings()), tableFont);

//        addCell(taxTable, "Standard Deduction (u/s 16)", tableFont);
//        addCell(taxTable, "50000.00", tableFont);

//        addCell(taxTable, "Net Taxable Income", tableFont);
//        addCell(taxTable, f(payroll.getTotalEarnings() - 50000), tableFont);

        document.add(taxTable);
        document.add(new Paragraph(" "));

        // Footer
        Paragraph footer = new Paragraph("This is a system generated payslip and does not require signature.", subHeaderFont);
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(20);
        document.add(footer);

        document.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    // Utility Methods
    private static void addCell(PdfPTable table, String content, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setPadding(5);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
    }

    private static void addHeaderCell(PdfPTable table, String content, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setBackgroundColor(new Color(220, 220, 220));
        cell.setPadding(6);
        table.addCell(cell);
    }

    private static void addDataRow(PdfPTable table,
                                   String c1, String c2, String c3, String c4, String c5,String c6, String c7,
                                   Font font) {
        table.addCell(new PdfPCell(new Phrase(c1, font)));
        table.addCell(new PdfPCell(new Phrase(c2, font)));
        table.addCell(new PdfPCell(new Phrase(c3, font)));
        table.addCell(new PdfPCell(new Phrase(c4, font)));
        table.addCell(new PdfPCell(new Phrase(c5, font)));
        table.addCell(new PdfPCell(new Phrase(c6, font)));
        table.addCell(new PdfPCell(new Phrase(c7, font)));

    }

    private static String f(double val) {
        return new DecimalFormat("#,##0.00").format(val);
    }

    private static final String[] units = {
            "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine",
            "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen",
            "Seventeen", "Eighteen", "Nineteen"
    };

    private static final String[] tens = {
            "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"
    };

    private static String convertAmountToWords(int number) {
        if (number == 0) return "Zero";
        return convert(number).trim();
    }

    private static String convert(int n) {
        if (n < 20) {
            return units[n];
        } else if (n < 100) {
            return tens[n / 10] + (n % 10 != 0 ? " " + units[n % 10] : "");
        } else if (n < 1000) {
            return units[n / 100] + " Hundred" + (n % 100 != 0 ? " and " + convert(n % 100) : "");
        } else if (n < 100000) {
            return convert(n / 1000) + " Thousand" + (n % 1000 != 0 ? " " + convert(n % 1000) : "");
        } else if (n < 10000000) {
            return convert(n / 100000) + " Lakh" + (n % 100000 != 0 ? " " + convert(n % 100000) : "");
        } else {
            return convert(n / 10000000) + " Crore" + (n % 10000000 != 0 ? " " + convert(n % 10000000) : "");
        }
    }



}