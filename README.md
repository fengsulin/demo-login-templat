:::info
日常中，我们的程序登录方式可能不止一种，比如用户名和密码登录、短信验证码登录、扫码登录等，如果每种都定义不同的接口去实现他们，就会增加代码冗余、维护难度和可读性。因此可以通过结合策略模式定义不同的登录策略，模板方法提供统一的登录实现。
:::
**_环境_**：

- `springboot`：`v3.0.5`

**_项目源码地址_**：`[https://github.com/fengsulin/demo-login-templat.git](https://github.com/fengsulin/demo-login-templat.git)`
**_项目整体结构图如下_**：
![image.png](https://cdn.nlark.com/yuque/0/2023/png/27231925/1679808429921-c041c5bb-d697-4957-baa8-45c35e11cb99.png#averageHue=%23798a5d&clientId=ub3deec83-40ad-4&from=paste&height=632&id=jv7OF&name=image.png&originHeight=790&originWidth=1106&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=156545&status=done&style=none&taskId=ua06e373d-87fd-4cfd-8b68-2f46e6b216c&title=&width=884.8)
## 一、依赖项
> 以`maven`项目为例，这里只需要一些常用的依赖

```bash
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

```
## 二、应用案例
### 2.1 模板方法定义
> 一个统一的登录需要做哪些处理？这里随便定义几个业务处理步骤

- 初始化登录参数（对登录的参数进行兼容转换）
- 登录前日志记录
- 校验验证码
- 自定义登录业务（策略模式）
- 黑名单校验
- 异地风控校验
- 用户权限校验
- 构建登录用户响应数据
- 记录登录信息
- 返回登录后的用户响应
### 2.2 工具类准备
#### 2.2.1 `JSON`转换工具类
```bash
/**
 * Json转换工具类
 */
public class JsonUtils {
    private static Logger log = LoggerFactory.getLogger(JsonUtils.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T str2Bean(String jsonStr,Class<T> destClazz){
        try {
            return objectMapper.readValue(jsonStr, destClazz);
        } catch (JsonProcessingException e) {
            log.error("json字符转实体异常：{}",e);
        }
        return null;
    }

    public static String bean2Str(Object origObj){
        try {
            return origObj == null ? null : objectMapper.writeValueAsString(origObj);
        } catch (JsonProcessingException e) {
            log.error("实体类转字符串异常，{}",e);
        }
        return null;
    }
}

```
#### 2.2.2 `Reques`请求体处理工具类
```bash
    /**
     * 读取请求体
     * @param request
     * @return
     */
    public static String getRequestJson(HttpServletRequest request){
        BufferedReader streamReader = null;
        try {
            streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = streamReader.readLine()) != null){
                sb.append(line);
            }
            System.out.println(sb.toString());
            return sb.toString();
        } catch (IOException e) {
            log.error("request请求体获取异常：{}",e);
        }finally {
            if (streamReader != null){
                try {
                    streamReader.close();
                } catch (IOException e) {
                    log.error("request请求体读取流关闭异常：{}",e);
                }
            }
        }
        return "";
    }
}
```
### 2.3 定义用户基本信息实体类
```bash
/**
 * 用户基本信息类
 */
@Data
public class UserInfoDto implements Serializable {
    /**
     * 用户名
     */
    private String username;

    /**
     * 用户id
     */
    private String userId;
}
```
### 2.4 定义统一登录接口
```bash
    /**
     * 用户统一登陆接口
     * @param loginForm：登录请求体的json字符串
     * @return
     */
    UserInfoDto userLogin(String loginForm);

    /**
     * 获取登录类型
     * @return
     */
    Integer getLoginType();
}

```
### 2.5 定义登录模板方法
```bash
/**
 * 登录模板
 */
@Slf4j
public abstract class LoginTemplate implements LoginAdapter{
    protected UserInfoDto userInfoDto;
    protected Integer loginType;

    @Override
    public UserInfoDto userLogin(String loginForm) {
        // 初始化登录参数
        this.initialize(loginForm);
        // 登录前日志记录
        this.logBefore();
        // 自定义登录
        UserInfoDto userLoginInfo = this.login();
        // 黑名单校验
        this.isBlacklist();
        // 异地登录风控校验
        this.risk();
        // 用户权限校验
        this.checkUserAuth();
        // 构建登录用户响应数据
        this.buildUserInfo(userLoginInfo);
        // 记录登录信息
        this.logAfter();
        // 登录完成
        return userInfoDto;
    }

    protected abstract void initialize(String loginFrom);
    protected abstract UserInfoDto login();

    private void logAfter(){
        log.info("logAfter . . . . . ");
    }
    private void logBefore(){
        log.info("logBefore . . . . . .");
    }

    private void buildUserInfo(UserInfoDto userLoginInfo){
        this.userInfoDto = userLoginInfo;
        log.info("buildUserInfo:{}",userLoginInfo);
    }

    private void checkUserAuth(){
        log.info("checkUserAuth . . . . . ");
    }

    private void risk(){
        log.info("risk . . . . .");
    }

    private void isBlacklist(){
        log.info("isBlacklist . . . . . ");
    }
}
```
### 2.6 定义策略类
#### 2.6.1 用户名密码请求类
```bash
/**
 * 用户秘密登录请求体
 */
@Data
public class AccountPwdLoginRo implements Serializable {
    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;
}

```
#### 2.6.2 定义用户名密码登录策略
```bash
/**
 * 用户名密码登录策略
 */
@Service
public class AccountPwdLogProcessor extends LoginTemplate {
    private AccountPwdLoginRo pwdLoginRo;

    @Override
    protected void initialize(String loginFrom) {
        AccountPwdLoginRo loginRo = JsonUtils.str2Bean(loginFrom, AccountPwdLoginRo.class);
        // 对模板的登录参数赋值，可在其他校验方法中使用（可根据需求）
        super.loginType = LoginTypeEnum.AccountPwd.getCode();
        // 对当前上下文登录参数赋值，可以在下面的登录方法中使用
        this.pwdLoginRo = loginRo;
    }

    @Override
    protected UserInfoDto login() {
        // 模拟账号密码登录
        String accountByDB = "admin";
        String passwordByDB = "123456";

        // 这里只做简单的校验
        if (this.pwdLoginRo.getAccount().equals(accountByDB) && this.pwdLoginRo.getPassword().equals(passwordByDB)){
            // 登录成功
            UserInfoDto userInfoDto = new UserInfoDto();
            userInfoDto.setUserId("0001");
            userInfoDto.setUsername("admin");
            return userInfoDto;
        }else {
            throw new RuntimeException("账号密码错误或账号不存在，登录失败");
        }
    }

    @Override
    public Integer getLoginType() {
        return LoginTypeEnum.AccountPwd.getCode();
    }
}

```
### 2.7 定义登录工厂
```bash
/**
 * 登录策略工厂类
 */
@Component
@Slf4j
public class LoginFactory implements ApplicationContextAware {

    private Map<Integer,LoginAdapter> processMap = new ConcurrentHashMap(3); // 默认有三种策略

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, LoginAdapter> tempMap = applicationContext.getBeansOfType(LoginAdapter.class);
        for (LoginAdapter value : tempMap.values()) {
            processMap.put(value.getLoginType(),value);
        }
    }

    public UserInfoDto userLogin(Integer loginType,String loginFrom){
        LoginAdapter loginAdapter = processMap.get(loginType);
        if (loginAdapter != null){
            return loginAdapter.userLogin(loginFrom);
        }else {
           log.error("{}：策略未找到",loginType);
           return null;
        }
    }
}

```
### 2.8 定义登录类型枚举
```bash
@Getter
public enum LoginTypeEnum {
    AccountPwd(0,"用户名密码登录"),
    AccountSms(1,"短信验证码登录");
    private final Integer code;
    private final String typeName;

    LoginTypeEnum(Integer code, String typeName) {
        this.code = code;
        this.typeName = typeName;
    }

    /**
     * 根据code获取typeName
     * @param code
     * @return
     */
    public static String getTypeNameByCode(Integer code){
        if (Objects.nonNull(code)){
            for (LoginTypeEnum value : values()) {
                if (value.getCode().equals(code)){
                    return value.getTypeName();
                }
            }
        }
        return null;
    }
}

```
### 2.9 登录接口
```bash
@RestController
@RequestMapping("/user")
public class LoginController {

    @Resource
    private LoginFactory loginFactory;

    @PostMapping(value = "/login",produces = MediaType.APPLICATION_JSON_VALUE)
    public String login(@RequestParam("loginType") Integer loginType, HttpServletRequest request){
        UserInfoDto userInfoDto = loginFactory.userLogin(loginType, RequestUtils.getRequestJson(request));
        return JsonUtils.bean2Str(userInfoDto);
    }
}

```

