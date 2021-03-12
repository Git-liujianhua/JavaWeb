package com.atliu.service.impl;

import com.atliu.dao.BookDao;
import com.atliu.dao.OrderDao;
import com.atliu.dao.OrderItemDao;
import com.atliu.dao.impl.BookDaoImpl;
import com.atliu.dao.impl.OrderDaoImpl;
import com.atliu.dao.impl.OrderItemDaoImpl;
import com.atliu.pojo.*;
import com.atliu.service.OrderService;
import com.sun.org.apache.xpath.internal.operations.Or;
import com.sun.xml.internal.ws.wsdl.parser.MemberSubmissionAddressingWSDLParserExtension;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class OrderServiceImpl implements OrderService {

    private OrderDao orderDao = new OrderDaoImpl();
    private OrderItemDao orderItemDao = new OrderItemDaoImpl();
    private BookDao bookDao = new BookDaoImpl();
    @Override
    public String creatOrder(Cart cart, Integer userId) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        // System.out.println(sdf.format(date));
        //创建订单号====唯一性
        String orderId = System.currentTimeMillis() + "" + userId;
        //创建一个订单对象
        Order order = new Order(orderId,sdf.format(date),cart.getTotalPrice(),0,userId);
        //保存订单
        orderDao.saveOrder(order);

        // int i =  12 / 0;

        //遍历购物车中每一个商品项转换成为订单项保存到数据库
        for (Map.Entry<Integer, CartItem>entry : cart.getItems().entrySet()){
            //获取每一个购物车中的商品项
            CartItem cartItem = entry.getValue();
            //转换为每一个订单项
            OrderItem orderItem = new OrderItem(null,cartItem.getName(),cartItem.getCount(),cartItem.getPrice(),cartItem.getTotalPrice(),orderId);
            //保存订单项到数据库
            orderItemDao.saveOrderItem(orderItem);

            //更新库存和销量
            Book book = bookDao.queryBookById(cartItem.getId());
            book.setSales(book.getSales() + cartItem.getCount());
            book.setStock(book.getStock() - cartItem.getCount());
            bookDao.updateBook(book);
        }
        //清空购物车
        cart.clear();
        return orderId;
    }
}
