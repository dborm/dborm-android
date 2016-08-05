package org.dborm.android;

import android.database.Cursor;

import org.dborm.core.api.DataConverter;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by shk on 16/8/4.
 */
public class DataConverterSQLite implements DataConverter {

    /**
     * 最好不要使用基本数值类型，如支持Integer但是不能支持int，原因如下：<br>
     * 1.update的原则是忽略null字段<br>
     * 2.数据库取出的值未必会有值，没有值的时候应该对属性的初始值赋值为null
     */
    private final String SUPPORT_TYPE = "[String, Integer, Boolean, Date, Long, Float, Double, Short, Byte]";


    @Override
    public Object fieldValueToColumnValue(Object fieldValue) {
        if (fieldValue != null) {
            if (Date.class.equals(fieldValue.getClass())) {
                Date time = (Date) fieldValue;
                return time.getTime();//将Date类型的值存储为时间戳
            }
            if (Boolean.class.equals(fieldValue.getClass())) {
                return fieldValue.toString();//将Boolean类型的值存储为字符串
            }
        }
        return fieldValue;
    }

    @Override
    public Object columnValueToFieldValue(Object columnValue, Field field) {
        if (field != null && columnValue != null) {
            if (isDate(field.getType())) {
                long time = Long.parseLong(columnValue.toString());
                return new Date(time);
            }
        }
        return columnValue;
    }

    /**
     * 将数据列对应的值转换为Java属性类型的值
     *
     * @param cursor     结果集
     * @param columnName 列名称
     * @param field      属性对象
     * @return 该属性类型的值
     * @throws SQLException
     * @author dborm@cocho
     * @time 2013-5-5上午2:44:02
     */
    public Object columnValueToFieldValue(Cursor cursor, String columnName, Field field) {
        int columnIndex = cursor.getColumnIndex(columnName);
        if (!cursor.isNull(columnIndex)) {
            if (field == null) {
                return cursor.getString(columnIndex);
            }
            Class<?> type = field.getType();
            try {
                if (String.class.equals(type)) {
                    return cursor.getString(columnIndex);
                } else if (Integer.class.equals(type)) {
                    return cursor.getInt(columnIndex);
                } else if (Boolean.class.equals(type)) {
                    String booleanValue = cursor.getString(columnIndex);
                    if (booleanValue != null) {
                        return Boolean.parseBoolean(booleanValue);
                    }
                } else if (isDate(type)) {
                    return cursor.getLong(columnIndex);
                } else if (Long.class.equals(type)) {
                    return cursor.getLong(columnIndex);
                } else if (Float.class.equals(type)) {
                    return cursor.getFloat(columnIndex);
                } else if (isDouble(type)) {
                    return cursor.getDouble(columnIndex);
                } else if (isShort(type)) {
                    return cursor.getShort(columnIndex);
                } else if (isByte(type)) {
                    return cursor.getBlob(columnIndex);
                } else {
                    throw new UnsupportedOperationException("类[" + field.getDeclaringClass() + "]中的属性[" + field.getName() + "] 的类型名称为["
                            + type.getName() + "] 暂不支持!\n 暂时支持的类型名称：" + SUPPORT_TYPE);
                }
            } catch (Exception e) {
                throw new RuntimeException("将列名为[" + columnName + "] 的值转换为属性名为[" + field.getName() + "]的值时出错!", e);
            }
        }
        return null;
    }


    private boolean isFloat(Class<?> type) {
        return Float.class.equals(type);
    }

    private boolean isLong(Class<?> type) {
        return Long.class.equals(type);
    }

    private boolean isDouble(Class<?> type) {
        return Double.class.equals(type);
    }

    private boolean isDate(Class<?> type) {
        return Date.class.equals(type);
    }

    private boolean isShort(Class<?> type) {
        return Short.class.equals(type);
    }

    private boolean isByte(Class<?> type) {
        return type.isArray() && (type.getComponentType().equals(byte.class) || type.getComponentType().equals(Byte.class));
    }
}
