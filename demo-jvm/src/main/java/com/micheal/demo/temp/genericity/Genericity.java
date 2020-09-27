package com.micheal.demo.temp.genericity;

import java.util.ArrayList;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/9/15 21:40
 * @Description 泛型
 */
//有一个sayWhatFruit方法来表明这个成员变量属于哪一种水果
//需求：
//1.我要在使用它的时候才能知道这个变量属于哪个水果，怎么做？通过构造器
//2.我这个变了，我要根据传进来的参数类型来确定我这里变量的类型，
//通俗来讲就是比如我传进一个String类型，那么这里的fruit就是String类型的
//如果我传进一个Integer类型的,那么这里的fruit就是Integer类型的
//解决方法：使用Object,因为Object是所有类的父类
//3.我不输出，我要调用传进来的类的其他方法，也就是子类自己的方法而不是object中的方法
class GenericClass<T>{//使用<xx>你随便起名字，指明了这个类是泛型类,里面的名字你随意
    //但是最好用T来表示。因为T是Type的缩写表明他是一个类型
    //隐申：final关键字
	/*  用法：
	 *   1.使用在类上:final class A.表明这个类不能被继承
     *   2.使用在属性上：final String a;表明这个属性a也就是引用a不能改变，即永远指向它指的变量
     *   3.使用在方法上:final void method() {}表明这个方法不能被子类复写
	**/
    private T fruit;

    public GenericClass() {

    }

    public GenericClass(T fruit) {
        this.fruit = fruit;
    }

    //public void doSomething() {
    //使用这种方法，如果我这里有千千万万个类型，我是不是得写无数多个if？？
    //if(fruit instanceof String) {
    //	System.out.println(((String)fruit).toUpperCase());
    //}
    //java.lang.ClassCastException.why？？引起异常的原因是什么？
    //答案很明了，是因为在使用的时候我无法判断传进来的是什么参数。
    //System.out.println(((String)fruit).toUpperCase());//在java中直接输出一个对象的时候，使用的是
    //从Object中继承下载的toString()方法，默认继承下载的toString()方法输出的
    //值是对象的类名@然后加上对象
    //Hashcode getClass().getName() + "@" + Integer.toHexString(hashCode());
    //}

    //泛型可以指定在类上或者方法上，指明这个是泛型类或者是泛型方法
    public void doSomething() {
        System.out.println(((String)fruit).toUpperCase());
    }
    public void doOtherSomething_Type1(GenericClass<?> t) {
        System.out.println(t);
    }
    public void doOtherSomething_Type2() {

    }

}
//泛型:从字面上来讲，就是泛指一个类型，那么为啥JAVA搞出这种东西？？
//@SuppressWarnings("all")//这个表达式用来忽略编译器的警告里面的值很多自己查阅官网资料进行使用
@SuppressWarnings("all")
public class Genericity {

    public static void main(String[] args) {
        //未指定类型
        ArrayList arrayList = new ArrayList();
        arrayList.add(1);
        arrayList.add("hello");
//		for (Object object : arrayList) {
//			//System.out.println(object+1);报错证明java是强类型语言无法
//			//推测类型必须在声明使用时强制指定相对应的类型
//			//java.lang.ClassCastException
//			//why?因为"HELLO"是字符串不能转型为int类型所以报错
//			//System.out.println(((int)object)+1);
//		}
        //通过以上我们推出->泛型就是用来减去程序员编程出错而推出一种方法
        //通过泛型我们就可以在编译时就能检测出是否发生castException
        //为什么出现泛型？这个问题如果在面试中提起问问自己啊是否懂了？
        //泛型怎么用？
        //通过在类或者方法上使用
        new GenericClass<String>().doOtherSomething_Type1(new GenericClass<Object>());//指定后面的类型暗示了一个问题就是我的这个
        //引用类型必须是Integer的其他并没啥用,而通过匿名对象则可以不指定引用
        //genericClass.doOtherSomething_Type1("abc");
        //<>表明了这是指定类型,分两部分
        //1.GenericClass<String> genericClass,这部分表明了指明引用类型的泛型类型
        //2.new GenericClass<>()指明new对象的类型为指定的类型
        //genericClass.doSomething();
        //new GenericClass(1).doSomething();
        //GenericClass<?> strG=new GenericClass<>("str");//new 对象的时候没有指明存放对象的实际类型，会导致unsafe
        //也就是表明这个操作会发生castexception,而泛型就是用来解决这个问题的，所以说这样违背了泛型的最终目的，所以编译器
        //直接在编译时就提醒，这回导致这个异常发生
        //GenericClass<Object> integerG=strG;//这里可以看到为啥不行？因为我上面接收的是String的，所以下面我又用了另外一个类型
        //去接收，自然报错，因为你想想？如果我上面存的是String类型的 然后我把存放这个String类型的堆内存力的对象new GenericClass()
        //让一个接收整形的对象的声明去接收，那么会导致什么问题呢？我用string的对象来做整形对象的操作是不是会导致castexception。。
        //又有人问我，为啥object是所有类的父类，还是不能接收？为啥？因为，你想对象存的是string 我把它赋值给object自然没问题，但是
        //整形是不是object的子类，假如能用object来接收，那么我就直接用object来放整形变量，那这样子的话 ，堆内存这个对象内部是不是
        //就存储了String类型的和int类型，这样是不是又回归到
        //ArrayList arrayList = new ArrayList();
        //arrayList.add(1);
        //arrayList.add("hello");

        //需求：我要定义一个泛型的方法，这里面我只需要nubmer的子类：short,byte,long,double,int,float
        //怎么办？解决办法使用指定上限使用extends
        //genericMethod(1);
    }
    //泛型方法
    //这个方法是泛型的，也就是不知道是啥类型的，泛型方法可以独立存在，也就是不依赖于泛型类
//	public static <T extends Number> void genericMethod(T t) {//这里的t就是根据传进来的参数来决定类型
//		System.out.println(((Number)t));
//	}
}
