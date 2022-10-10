package com.diantongren.utils;

import org.apache.ibatis.type.*;

import java.sql.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * postgreSQL bigint[] to List<Object> typeHandler
 *
 * @author rumda
 */
@MappedJdbcTypes(JdbcType.ARRAY)
@MappedTypes(List.class)
public class PostgresArrayTypeHandler extends BaseTypeHandler<List<Object>> {
    /**
     * 这个是 Java 类型往数据库存储时的处理逻辑
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Object> parameter, JdbcType jdbcType) throws SQLException {
        // 如果是空数组的话也给它塞个空数组进去，在 PostgreSQL 中是 {} 这种，应该可以根据业务上的需求调整
        if (parameter.isEmpty()) {
            Connection conn = ps.getConnection();
            Array array = conn.createArrayOf(JDBCType.BIGINT.getName(), new Long[0]);
            ps.setArray(i, array);
            array.free();
            return;
        }

        String typeName = null;
        if (parameter.get(0) instanceof Integer) {
            typeName = JDBCType.INTEGER.getName();
        } else if (parameter.get(0) instanceof String) {
            typeName = JDBCType.VARCHAR.getName();
        } else if (parameter.get(0) instanceof Boolean) {
            typeName = JDBCType.BOOLEAN.getName();
        } else if (parameter.get(0) instanceof Double) {
            typeName = JDBCType.DOUBLE.getName();
        } else if (parameter.get(0) instanceof Float) {
            typeName = JDBCType.FLOAT.getName();
        } else if (parameter.get(0) instanceof Long) {
            typeName = JDBCType.BIGINT.getName();
        } else if (parameter.get(0) instanceof Short) {
            typeName = JDBCType.SMALLINT.getName();
        } else if (parameter.get(0) instanceof Byte) {
            typeName = JDBCType.TINYINT.getName();
        }
        if (typeName == null) {
            throw new TypeException("ArrayTypeHandler parameter typeName error, your type is " + parameter.getClass().getName());
        }

        Connection conn = ps.getConnection();
        Array array = conn.createArrayOf(typeName, parameter.toArray());
        ps.setArray(i, array);
        array.free();
    }

    @Override
    public List<Object> getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return getList(resultSet.getArray(s));
    }

    @Override
    public List<Object> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return getList(resultSet.getArray(i));
    }

    @Override
    public List<Object> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return getList(callableStatement.getArray(i));
    }

    /**
     * 上面都是从数据库读取后存入 Java PO 对象时的处理逻辑
     * 这里写了一个统一的方法，因为逻辑都一样
     */
    private List<Object> getList(Array array) throws SQLException {
        // 如果数据库字段为 null，这里会转成一个空的 List，根据业务需求更改，赋值为 null 的话，后面对 PO 处理要注意 NPE
        if (array == null) {
            return Collections.emptyList();
        }

        // 这里要把 java.sql.Array 转成 java.util.List，尝试了多种写法，只有 Copilot 写的不会出警告
        Object[] objects = (Object[]) array.getArray();
        array.free();
        return Arrays.stream(objects).collect(Collectors.toList());
    }
}
