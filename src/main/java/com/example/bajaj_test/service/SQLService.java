package com.example.bajaj_test.service;

import org.springframework.stereotype.Service;

@Service
public class SQLService {

    public String getFinalQuery(String regNo) {
        String digits = regNo.replaceAll("\\D", "");
        int lastTwo = Integer.parseInt(digits.substring(Math.max(0, digits.length()-2)));
        return (lastTwo % 2 == 0) ? solveEvenQuestion() : solveOddQuestion();
    }

    private String solveOddQuestion() {
        return """
            SELECT 
                e.EMP_ID,
                e.FIRST_NAME,
                e.LAST_NAME,
                d.DEPARTMENT_NAME,
                (SELECT COUNT(*) 
                    FROM EMPLOYEE e2 
                    WHERE e2.DEPARTMENT = e.DEPARTMENT 
                    AND e2.DOB > e.DOB) AS YOUNGER_EMPLOYEES_COUNT
            FROM EMPLOYEE e
            JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID
            ORDER BY e.EMP_ID DESC;
            """;
    }


    private String solveEvenQuestion() {
        return """
            SELECT 
                e.EMP_ID,
                e.FIRST_NAME,
                e.LAST_NAME,
                d.DEPARTMENT_NAME,
                (SELECT COUNT(*) 
                    FROM EMPLOYEE e2 
                    WHERE e2.DEPARTMENT = e.DEPARTMENT 
                    AND e2.DOB > e.DOB) AS YOUNGER_EMPLOYEES_COUNT
            FROM EMPLOYEE e
            JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID
            ORDER BY e.EMP_ID DESC;
            """;
    }

}
