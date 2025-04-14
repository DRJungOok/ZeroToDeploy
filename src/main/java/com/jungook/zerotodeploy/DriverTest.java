package com.jungook.zerotodeploy;

public class DriverTest {
	public static void main(String[] args) {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			System.out.println("✅ MariaDB 드라이버 로딩 성공!");
		} catch (ClassNotFoundException e) {
			System.out.println("❌ 드라이버 로딩 실패!");
		}
	}
}
