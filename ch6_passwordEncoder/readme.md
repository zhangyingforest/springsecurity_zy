问题: 目前的密码是以明文方式存储
解决方案: 使用md5对密码进行加密存储.  

破解方案: 
1. 反查表. 
    md5码     密码
2. 升级:  彩虹表
    彩虹表原理:  https://www.zhihu.com/question/19790488/answer/1893671941
更好的加密:  加盐: 
     1)   多加密几次
     2)   md5( 特定长度随机数+password )
     3)   BCrypt算法: 
       密码格式:   $2a$12$前22位是随机盐值+31位真正的散列值  
       解释: $2a =>算法版本 , 如2a版本加入了对非ASCII字符及空终止符的处理
            $12 =>成本参数，表明该密文需要迭代的次数，　　2^12 即 4092次
            
       https://baijiahao.baidu.com/s?id=1709586034894716487&wfr=spider&for=pc
       

Security内置的加密机制接口:  PasswordEncoder，它有两个方法: encode(),  matches()
   它的实现类: BCryptPasswordEncoder,  Pbkdf2PasswordEncoder(类似于BCrypt的慢散列加密),  StandardPasswordEncoder常规摘要算法(SHA256)
       
       
