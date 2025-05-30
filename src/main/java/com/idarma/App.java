package com.idarma;

import io.github.cdimascio.dotenv.Dotenv;

public class App 
{
    public static void main(String[] args) {
        Dotenv.load();
        new LoginPage();
    }
}
