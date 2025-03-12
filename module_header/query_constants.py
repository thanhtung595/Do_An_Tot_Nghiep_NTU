GET_ALL_HEADER = """
    SELECT * FROM HeaderLayout
    WHERE IsToken = %s
        OR IsDefault = '1'
"""