package com.huawei.demo.device;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.huawei.demo.auth.Authentication;
import com.huawei.util.Constants;
import com.huawei.util.HttpUtils;
import com.huawei.util.StreamClosedHttpResponse;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
public class frameIOT {
    static Connection con=null;
    static Statement statement=null;
    static PreparedStatement preparedStatement=null;
    public static void main(String[] args)throws NoSuchAlgorithmException, KeyManagementException, IOException, URISyntaxException {
        //小编的Java水平有限，也在学习当中，程序也有很多bug，仅供大家参考，大家运行前，请确保按照帖子要求正确完成了所有内容
        //关注公众号“IOT趣制作”，程序优化完善后会第一时间公布
        final String project_id = "33333333333332333";      //项目ID
        final String testdev_id = "33322263338882333";      //设备ID
        String protest="temp";      //设备属性

        final JFrame window = new JFrame();
        window.setTitle("华为云应用侧开发 JAVA Demo测试");//窗口名称：“华为云应用侧开发 JAVA Demo测试”
        window.setBounds(350,350,500,500);//设置窗口在屏幕上的位置和大小
        window.setLocationRelativeTo(null);//把上面写的x,y位置改为窗口居中
        window.setLayout(null);

        JLabel jlbdevID = new JLabel("设备ID:");
        final JTextField jtfdevID = new JTextField(testdev_id);
        jlbdevID.setBounds(50,100,50,40);
        jtfdevID.setBounds(100,100,300,40);

        JLabel jlbpro = new JLabel("属性：");
        final JTextField jtfpro= new JTextField(protest);
        jlbpro.setBounds(50,150,50,40);
        jtfpro.setBounds(100,150,300,40);

        JLabel jlbrestip = new JLabel("查询结果：");
        final JLabel jlbres = new JLabel("第一次查询请稍等3s");
        jlbrestip.setBounds(50,200,100,40);
        jlbres.setBounds(130,200,150,40);

        JButton btn_search = new JButton("查询");
        JButton btn_close = new JButton("关闭");
        btn_search.setBounds(100,250,60,40);
        btn_close.setBounds(250,250,60,40);

        window.add(jlbdevID);//把“设备ID:”标签添加到窗口中
        window.add(jtfdevID);//设备ID的框添加到窗口中
        window.add(jlbpro);//把“属性：”标签添加到窗口中
        window.add(jtfpro);//把属性的输入框添加到窗口中
        window.add(jlbrestip);//把“查询结果：”标签添加到窗口中
        window.add(jlbres);//把查询结果标签添加到窗口中
        window.add(btn_search);//把查询按钮添加到窗口中
        window.add(btn_close);//把关闭按钮添加到窗口中


        window.setVisible(true);//窗口设置为可见
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//释放当前窗口

        btn_search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent btn1e) {
                //例1：查询在线状态
                String token = null;
                try {
                    token = Authentication.getToken();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String url = Constants.DEVICE_COMMAND_URL;                        //查询设备
                url = String.format(url, project_id,jtfdevID.getText());
                Map<String, String> header = new HashMap<String, String>();
                header.put("Content-Type", "application/json");
                header.put("X-Auth-Token", token);

                Map<String, String> params = new HashMap<String, String>();
                params.put("device_id", jtfdevID.getText());
                HttpUtils httpUtils = new HttpUtils();
                try {
                    httpUtils.initClient();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                StreamClosedHttpResponse httpResponse = null;
                try {
                    httpResponse = httpUtils.doGet(url, header, params);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                String getStatusLine=httpResponse.getStatusLine().toString();
                String getContent=httpResponse.getContent();
                System.out.println(getStatusLine);
                System.out.println(getContent);

                //解析json
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = null;
                try {
                    jsonNode = objectMapper.readValue(getContent, JsonNode.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JsonNode statusNode = jsonNode.get("status");
                String statusstr = statusNode.asText();
                System.out.println("status = " + statusstr);
                if(statusstr.equals("OFFLINE"))
                {
                    statusstr="设备离线";
                    System.out.println(statusstr);
                }
                else if(statusstr.equals("ONLINE"))
                {
                    statusstr="设备在线";
                    System.out.println(statusstr);
                }

                url = Constants.DEVICE_SHADOW_URL;                         //查询设备影子
                url = String.format(url, project_id,jtfdevID.getText());
                header = new HashMap<String, String>();
                header.put("Content-Type", "application/json");
                header.put("X-Auth-Token", token);

                params = new HashMap<String, String>();
                params.put("device_id", jtfdevID.getText());
                httpUtils = new HttpUtils();
                try {
                    httpUtils.initClient();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                httpResponse = null;
                try {
                    httpResponse = httpUtils.doGet(url, header, params);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                getStatusLine=httpResponse.getStatusLine().toString();
                getContent=httpResponse.getContent();
                System.out.println(getStatusLine);
                System.out.println(getContent);
                objectMapper = new ObjectMapper();
                jsonNode = null;
                try {
                    jsonNode = objectMapper.readValue(getContent, JsonNode.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JsonNode tempNode = jsonNode.get("shadow").get(0).get("reported").get("properties").get(jtfpro.getText());
                String tempstr = tempNode.asText();
                System.out.println(jtfpro.getText()+"=" + tempstr);
                System.out.println("温度：" + tempstr+"℃");
                jlbres.setText(statusstr+"，属性："+tempstr);
            }
        });
        btn_close.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent btn2e) {
                window.dispose();
            }
        });

    }

}
