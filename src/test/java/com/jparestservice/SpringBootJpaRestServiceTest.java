package com.jparestservice;


import com.jparestservice.entities.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class SpringBootJpaRestServiceTest {

    @LocalServerPort
    private int port;

    RestTemplate restTemplate = new RestTemplate();

    private String getRootUrl(){
        return "http://localhost:"+port;
    }

    @Test
    public void testGetAllEmployee()
    {
        ResponseEntity<Employee[]> responseEntity = restTemplate.getForEntity(getRootUrl()+"/employee", Employee[].class);
        List<Employee> posts = Arrays.asList(responseEntity.getBody());
        assertNotNull(posts);

    }

    public void testGetEmployeeById()
    {
        Employee emp = restTemplate.getForObject(getRootUrl()+"/employee/1", Employee.class);
        assertNotNull(emp);
    }


    @Test
    public void testCreateEmployee()
    {
        Employee emp = new Employee();
        emp.setName("Duck");
        emp.setEmail("duck@gmail.com");


        ResponseEntity<Employee> empResponse = restTemplate.postForEntity(getRootUrl()+"/employee", emp, Employee.class);
        assertNotNull(empResponse);
        assertNotNull(empResponse.getBody());
    }


    @Test
    public void testUpdateEmployee()
    {
        int id = 1;
        Employee emp = restTemplate.getForObject(getRootUrl()+"/employee/"+id, Employee.class);
        emp.setName("Mark");
        emp.setEmail("markk@gmail.com");

        restTemplate.put(getRootUrl()+"/employee/"+id, emp);

        Employee updatedEmployee= restTemplate.getForObject(getRootUrl()+"employee/"+id, Employee.class);
        assertNotNull(updatedEmployee);
    }

    @Test
    public void testDeleteEmployee()
    {
        int id = 2;
        Employee emp = restTemplate.getForObject(getRootUrl()+"/employee/"+id, Employee.class);
        assertNotNull(emp);

        //restTemplate.delete(getRootUrl()+"/employee/"+id);

        try {
            emp = restTemplate.getForObject(getRootUrl()+"/employee/"+id, Employee.class);
        }
        catch (final HttpClientErrorException e) {
            assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
        }
    }


}
