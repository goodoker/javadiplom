package ru.netology.graphics;

import ru.netology.graphics.image.TextGraphicsConverter;
import ru.netology.graphics.server.GServer;
import ru.netology.graphics.image.Converter;

import java.io.File;
import java.io.PrintWriter;


public class Main {
    public static void main(String[] args) throws Exception {
        TextGraphicsConverter converter = new Converter(); // Создайте тут объект вашего класса конвертера

        GServer server = new GServer(converter); // Создаём объект сервера
        server.start(); // Запускаем

        // Или то же, но с выводом на экран:
//        String url = "https://lingua-surgut.ru/wp-content/uploads/2021/09/actionAndroid.png";
//        String imgTxt = converter.convert(url);
//        System.out.println(imgTxt);

    }
}