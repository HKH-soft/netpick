package com.hossein.spring_project.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

public class CustomerRowMapperTest {

    @Test
    void testMapRow() throws SQLException {
        
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();

        ResultSet resualtSet = mock(ResultSet.class);
        when(resualtSet.getInt("id")).thenReturn(1);
        when(resualtSet.getString("name")).thenReturn("name");
        when(resualtSet.getString("email")).thenReturn("email");
        when(resualtSet.getInt("age")).thenReturn(19);
        when(resualtSet.getBoolean("gender")).thenReturn(false);

        Customer actual = customerRowMapper.mapRow(resualtSet,1);

        Customer expected = new Customer(
            1,"name","email",19,false
        );
        assertThat(actual).isEqualTo(expected);
    }
}
