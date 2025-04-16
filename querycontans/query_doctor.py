CREATE_DOCTOR = """
    INSERT INTO Doctors(FullName, PhoneNumber, Email, Address, UserID, DepartmentID, Experience, image) VALUES
        (%s, %s, %s, %s, %s, %s, %s, 'img/doctor/avatar_default.jpg')
    RETURNING ID
"""

SELECT_DOCTOR_ALL = """
    SELECT  
        u.username
        ,d.id
        ,d.fullname
        ,d.email
        ,d.phonenumber
        ,d.experience
        ,d.image
        ,d.address
        ,detm.name
        ,d.departmentid
        ,CASE 
            WHEN COUNT(s.id) = 0 THEN 'Chưa xét lịch'
            ELSE STRING_AGG(CONCAT('Thứ ', s.workday), ' - '  ORDER BY s.workday)
        END AS workdays
        ,CASE 
            WHEN COUNT(s.id) = 0 THEN 'Chưa xét lịch'
            ELSE STRING_AGG(
            DISTINCT CONCAT(
                TO_CHAR(s.starttime, 'HH24:MI AM'), 
                ' - ', 
                TO_CHAR(s.endtime, 'HH24:MI PM')
            ), ' | '
            )
        END AS timeOnline
    FROM Doctors d
        INNER JOIN Users u
            ON d.userid = u.id
        INNER JOIN Departments detm
            ON d.departmentid = detm.id
        LEFT JOIN Schedule s
            ON d.id = s.doctorid
    GROUP BY 
        d.id
        ,u.username
        ,detm.name
        ,detm.id
"""

INSERT_SCHEDULE = """
    INSERT INTO Schedule(doctorid, workday, starttime, endtime) VALUES
    (%s, %s, %s, %s)
    RETURNING ID
"""

UPDATE_DOCTOR_BY_ID = """
    UPDATE Doctors 
    SET 
        fullname = %s
        ,email = %s
        ,phonenumber = %s
        ,address = %s
        ,experience = %s
        ,departmentid = %s
    WHERE Id = %s
"""

DELETE_SCHEDULE_BT_ID_DOCTOR = """
    DELETE FROM Schedule
    WHERE 
        doctorid = %s
"""

SELECT_DOCTOR_ALL_VI = """
    SELECT  
         d.id as id
        ,u.username as taikhoan
        ,d.fullname as ten
        ,d.email as email
        ,d.phonenumber as phone
        ,d.experience as namkinhnghiem
        ,d.image as hinhAnh
        ,d.address as diachi
        ,detm.name as khoa
        ,d.departmentid as khoaid
        ,CASE 
            WHEN COUNT(s.id) = 0 THEN 'Chưa xét lịch'
            ELSE STRING_AGG(CONCAT('Thứ ', s.workday), ' - '  ORDER BY s.workday)
        END AS lichlamviec
        ,CASE 
            WHEN COUNT(s.id) = 0 THEN 'Chưa xét lịch'
            ELSE STRING_AGG(
            DISTINCT CONCAT(
                TO_CHAR(s.starttime, 'HH24:MI AM'), 
                ' - ', 
                TO_CHAR(s.endtime, 'HH24:MI PM')
            ), ' | '
            )
        END AS thoigianlamviec
    FROM Doctors d
        INNER JOIN Users u
            ON d.userid = u.id
        INNER JOIN Departments detm
            ON d.departmentid = detm.id
        LEFT JOIN Schedule s
            ON d.id = s.doctorid
    GROUP BY 
        d.id
        ,u.username
        ,detm.name
        ,detm.id
"""