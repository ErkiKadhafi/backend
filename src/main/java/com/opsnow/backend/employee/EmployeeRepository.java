package com.opsnow.backend.employee;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer>, JpaSpecificationExecutor<Employee> {
    Optional<Employee> findByEmpNo(Integer empNo);

    Employee deleteByEmpNo(Integer empNo);

    @Query(value = "SELECT \n" + //
            "    e1.departmentcode,\n" + //
            "    e1.empno,\n" + //
            "    e1.empname,\n" + //
            "    (\n" + //
            "        SELECT SUM(e2.salary)\n" + //
            "        FROM employee e2\n" + //
            "        WHERE e2.departmentcode = e1.departmentcode\n" + //
            "          AND e2.empno <= e1.empno\n" + //
            "    ) AS cumulative_salary\n" + //
            "FROM employee e1\n" + //
            "ORDER BY e1.departmentcode, e1.empno;", nativeQuery = true)
    List<Map<String, Object>> getEmployeeCumulativeSalary();

    @Query(value = "SELECT \n" + //
            "    l.locationname AS \"location_name\",\n" + //
            "    (\n" + //
            "        SELECT d2.departmentname\n" + //
            "        FROM department d2\n" + //
            "        JOIN employee e ON d2.departmentcode = e.departmentcode\n" + //
            "        WHERE e.locationcode = l.locationcode\n" + //
            "        GROUP BY d2.departmentname\n" + //
            "        ORDER BY COUNT(e.empno) DESC\n" + //
            "        LIMIT 1\n" + //
            "    ) AS \"most_employee_dept\",\n" + //
            "    (\n" + //
            "        SELECT COUNT(e3.empno)\n" + //
            "        FROM employee e3\n" + //
            "        JOIN department d3 ON e3.departmentcode = d3.departmentcode\n" + //
            "        WHERE e3.locationcode = l.locationcode\n" + //
            "        AND e3.departmentcode = (\n" + //
            "            SELECT d2.departmentcode\n" + //
            "\t        FROM department d2\n" + //
            "\t        JOIN employee e ON d2.departmentcode = e.departmentcode\n" + //
            "\t        WHERE e.locationcode = l.locationcode\n" + //
            "\t        GROUP BY d2.departmentcode\n" + //
            "\t        ORDER BY COUNT(e.empno) DESC\n" + //
            "\t        LIMIT 1\n" + //
            "        )\n" + //
            "    ) AS \"employee_dept_count\",\n" + //
            "    (\n" + //
            "        SELECT AVG(e5.salary)\n" + //
            "        FROM employee e5\n" + //
            "        WHERE e5.locationcode = l.locationcode\n" + //
            "          AND e5.departmentcode = (\n" + //
            "              SELECT d6.departmentcode\n" + //
            "              FROM employee e6\n" + //
            "              JOIN department d6 ON e6.departmentcode = d6.departmentcode\n" + //
            "              GROUP BY d6.departmentcode\n" + //
            "              ORDER BY AVG(e6.salary) ASC\n" + //
            "              LIMIT 1\n" + //
            "          )\n" + //
            "    ) AS \"avg_salary\"\n" + //
            "FROM location l\n" + //
            "ORDER BY l.locationname;", nativeQuery = true)
    List<Map<String, Object>> getAverageSalaryByLocationForLowestSalaryDepartment();

    @Query(value = "SELECT\n" + //
            "\tl.locationname AS \"locationname\",\n" + //
            "\td.departmentname AS \"departmentname\",\n" + //
            "\te.empname AS \"empname\",\n" + //
            "\tt.tiername AS \"tiername\",\n" + //
            "\te.salary AS \"salary\",\n" + //
            "\t/* Count how many perople with higher salary */\n" + //
            "\t(\n" + //
            "\t    SELECT COUNT(DISTINCT e2.salary)\n" + //
            "\t    FROM employee e2\n" + //
            "\t    JOIN department d2 ON e2.departmentcode = d2.departmentcode\n" + //
            "\t    JOIN location l2 ON e2.locationcode = l2.locationcode\n" + //
            "\t    WHERE l2.locationname = l.locationname\n" + //
            "\t      AND d2.departmentname = d.departmentname\n" + //
            "\t      AND e2.salary > e.salary\n" + //
            "\t) + 1 AS \"rank\",\n" + //
            "\t/* Count the gap from prev one, rule:\n" + //
            "\t * 1. If tied -> 0\n" + //
            "\t * 2. If rank 1 -> 0\n" + //
            "\t * 3. Else Count actual gap*/\n" + //
            "\t(\n" + //
            "\t\tCASE\n" + //
            "\t\t  WHEN EXISTS (\n" + //
            "\t\t    SELECT 1\n" + //
            "\t\t    FROM employee e_same\n" + //
            "\t\t    WHERE e_same.locationcode   = e.locationcode\n" + //
            "\t\t      AND e_same.departmentcode = e.departmentcode\n" + //
            "\t\t      AND e_same.salary         = e.salary\n" + //
            "\t\t      AND e_same.empno <> e.empno\n" + //
            "\t\t  ) THEN 0\n" + //
            "\t\t  WHEN NOT EXISTS (\n" + //
            "\t\t    SELECT 1\n" + //
            "\t\t    FROM employee e_h\n" + //
            "\t\t    WHERE e_h.locationcode   = e.locationcode\n" + //
            "\t\t      AND e_h.departmentcode = e.departmentcode\n" + //
            "\t\t      AND e_h.salary > e.salary\n" + //
            "\t\t  ) THEN 0\n" + //
            "\t\t  ELSE (\n" + //
            "\t\t    (SELECT MIN(e_next.salary)\n" + //
            "\t\t     FROM employee e_next\n" + //
            "\t\t     WHERE e_next.locationcode   = e.locationcode\n" + //
            "\t\t       AND e_next.departmentcode = e.departmentcode\n" + //
            "\t\t       AND e_next.salary > e.salary\n" + //
            "\t\t    )\n" + //
            "\t\t    - e.salary\n" + //
            "\t\t  )\n" + //
            "\t\tEND \n" + //
            "\t) AS \"salarygap\"\n" + //
            "FROM employee e\n" + //
            "JOIN department d ON e.departmentcode = d.departmentcode\n" + //
            "JOIN location l ON e.locationcode = l.locationcode\n" + //
            "JOIN tier t ON e.tiercode = t.tiercode\n" + //
            "ORDER BY l.locationname, d.departmentname, e.salary DESC;", nativeQuery = true)
    List<Map<String, Object>> getSalaryGap();
}
