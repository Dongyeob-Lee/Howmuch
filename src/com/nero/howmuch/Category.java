package com.nero.howmuch;


public class Category {
//	public static final String[] BIG_CATEGORY = { "디지털/가전/컴퓨터", "스포츠/레저/자동차" };
	public static final String[] CATEGORIES = { "남성의류",
			"여성의류", "아동복", "속옷", "신발",
			"가방", "보석", "악세사리", "화장품",
			"미용용품", "생활잡화", "농수산물(정육)",
			"건강식품", "가공식품", "가구", "전자제품",
			"철물(공구)", "자동차","스포츠(레저)","차량용품",
			"도서","문구","취미","상품권","중고물품","공사(토목,건축)","리모델링",
			"산업용 원재료","철강(금속)","DIY","꽃(식물)","오토바이","오토바이용품","여행","설치 및 장착","부동산","애완동물","의료용품"};
//	public static String[] small_category_digital={"TV/냉장고/세탁기",
//			"주방/이미용/생활가전", "에어컨/선풍기/제습기", "음향/AV/사무가전", "노트북/데스크탑",
//			"모니터/프린터/저장장치"};
//	public static String[] small_category_sports = {"스포츠의류/운동화/용품", "등산/아웃도어/캠핑/낚시",
//			"자전거/헬스/다이어트", "구기/수영/스키/체험레저", "골프클럽/의류/용품", "자동차용품",
//			"블랙박스/네비/하이패스", "공구/산업용품/도어락"};
//	public static String[] getSmallCategory(int big_category_num) {
//		String[] result=null;
//		if (big_category_num == 0) {
//			result= small_category_digital;
//		} else if (big_category_num == 1) {
//			result= small_category_sports;
//		}
//		return result;
//	}
	
	//카테고리 이름으로 카테고리 번호 추출
	public static int getSmallCategoryNumByName(String small_name){
		int small_num=0;
		for(int i=0; i<CATEGORIES.length; i++){
			if(CATEGORIES[i].equals(small_name)){
				small_num = i;
			}
		}
		return small_num;
	}
//	public static String getBigCategoryNameBySmallNum(int small_num){
//		String result=null;
//		String small_name = SMALL_CATEGORIES[small_num];
//		for(int i=0; i<small_category_digital.length; i++){
//			if(small_category_digital.equals(small_name)){
//				result=BIG_CATEGORY[0];
//			}
//		}
//		for(int i=0; i<small_category_sports.length; i++){
//			if(small_category_sports.equals(small_name)){
//				result=BIG_CATEGORY[1];
//			}
//		}
//		return result;
//	}
}
