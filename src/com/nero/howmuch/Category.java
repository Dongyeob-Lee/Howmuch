package com.nero.howmuch;


public class Category {
//	public static final String[] BIG_CATEGORY = { "������/����/��ǻ��", "������/����/�ڵ���" };
	public static final String[] CATEGORIES = { "�����Ƿ�",
			"�����Ƿ�", "�Ƶ���", "�ӿ�", "�Ź�",
			"����", "����", "�Ǽ��縮", "ȭ��ǰ",
			"�̿��ǰ", "��Ȱ��ȭ", "����깰(����)",
			"�ǰ���ǰ", "������ǰ", "����", "������ǰ",
			"ö��(����)", "�ڵ���","������(����)","������ǰ",
			"����","����","���","��ǰ��","�߰�ǰ","����(���,����)","���𵨸�",
			"����� �����","ö��(�ݼ�)","DIY","��(�Ĺ�)","�������","������̿�ǰ","����","��ġ �� ����","�ε���","�ֿϵ���","�Ƿ��ǰ"};
//	public static String[] small_category_digital={"TV/�����/��Ź��",
//			"�ֹ�/�̹̿�/��Ȱ����", "������/��ǳ��/������", "����/AV/�繫����", "��Ʈ��/����ũž",
//			"�����/������/������ġ"};
//	public static String[] small_category_sports = {"�������Ƿ�/�ȭ/��ǰ", "���/�ƿ�����/ķ��/����",
//			"������/�ｺ/���̾�Ʈ", "����/����/��Ű/ü�跹��", "����Ŭ��/�Ƿ�/��ǰ", "�ڵ�����ǰ",
//			"���ڽ�/�׺�/�����н�", "����/�����ǰ/�����"};
//	public static String[] getSmallCategory(int big_category_num) {
//		String[] result=null;
//		if (big_category_num == 0) {
//			result= small_category_digital;
//		} else if (big_category_num == 1) {
//			result= small_category_sports;
//		}
//		return result;
//	}
	
	//ī�װ� �̸����� ī�װ� ��ȣ ����
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
