SELECT_DOCTOR_ALL_VI = """
    SELECT  
        d.id as id
        ,u.username as taikhoan
        ,u.fullname as ten
        ,u.email as email
        ,u.phonenumber as phone
        ,d.experience as namkinhnghiem
        ,'https://ntt-datn-clinic-management.s3.ap-southeast-1.amazonaws.com/avatar/doctor/' || u.image as hinhAnh
        ,u.address as diachi
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
        ,u.fullname
        ,u.email
        ,u.phonenumber
        ,u.image
        ,u.address
        ,detm.name
        ,detm.id
"""