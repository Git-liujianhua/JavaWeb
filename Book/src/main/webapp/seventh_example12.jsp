<%@ page import="java.sql.*" %>
<%@ page contentType="text/html;charset=gb2312" %>
<HTML><body bgcolor=AAEF9E><font size=2>
<%  Connection con=null;
    Statement stat; 
    ResultSet rs;
	try{
		  Class.forName("com.mysql.jc.jdbc.Driver");
    
		  int n=100;
          String uri="jdbc:mysql://localhost:3306/bank?"+"user=root&password=root&characterEncoding=gb2312";
          con=DriverManager.getConnection(uri);
          con.setAutoCommit(false);  //�ر��Զ��ύģʽ
          stat=con.createStatement();
          rs=stat.executeQuery("SELECT userMoney FROM user WHERE name='A'");
          rs.next();
          double aMoney=rs.getDouble("userMoney");
          rs=stat.executeQuery("SELECT userMoney FROM user WHERE name='B'");
          rs.next();
          double bMoney=rs.getDouble("userMoney");
          out.print("ת��ǰA��userMoney��ֵ��"+aMoney+"<br>");
          out.print("ת��ǰB��userMoney��ֵ��"+bMoney+"<br>");
          aMoney=aMoney-n;
          if(aMoney>=0) {
            bMoney=bMoney+n;
            stat.executeUpdate("UPDATE user SET userMoney ="+aMoney+" WHERE name='A'");
            stat.executeUpdate("UPDATE user SET userMoney="+bMoney+" WHERE name='B'");
            int a=1/0;
            con.commit();                 //��ʼ������
            
          }
          rs=stat.executeQuery("SELECT * FROM user WHERE name='A'||name='B'"); 
          out.println("<b>ת�˺���������:<br>"); 
          while(rs.next()) {
              out.print(rs.getString(1)+"	");
              out.print(rs.getString(2)); 
              out.print("<br>");
          }
          
     }
     catch(Exception e){
         con.rollback();           //�������������Ĳ���
         out.println(e);
     }finally{
		 con.close();
	 }
%>
</font></body></HTML>
