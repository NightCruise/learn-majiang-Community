package life.community.enums;

import java.util.List;

public enum CategoryEnum {

    FRONT_END("前端", List.of("前端", "javascript", "typescript", "ecmascript-6", "css", "css3", "html", "html5",
            "node.js", "npm", "react.js", "vue.js", "angular", "chrome", "chrome-devtools", "safari", "webkit",
            "edge", "bootstrap", "tailwind-css", "antd", "sass", "less", "postcss", "yarn", "postman", "fiddler")),
    BACK_END("后端", List.of("后端", "go", "php", "lavarel", "swoole", "java", "spring", "springboot", "node.js",
            "express", "python", "flask", "django", "fastapi", "c", "c++", "c#", "ruby", "ruby-on-rail", "sasp",
            ".net", "scala", "rust", "爬虫")),
    MOBILE("移动端", List.of("android", "android-studio", "java", "kotlin", "ios", "swift",
            "objective-c", "xcode", "flutter", "react-native", "webapp", "小程序")),
    DATA("数据", List.of("数据库", "mysql", "mariadb", "postgresql", "sqlite", "sql", "nosql", "redis",
            "mongodb", "json", "yaml", "xml", "elasticsearch", "memcached")),
    OPS("运维", List.of("运维", "微服务", "服务器", "linux", "ubuntu", "debian", "nginx", "apache", "docker",
            "容器", "kubernetes", "devops", "serverless", "负载均衡", "ssh", "jenkin", "svagrant")),
    AI("AI", List.of("算法", "机器学习", "人工智能", "深度学习", "数据挖掘",
            "tensorflow", "pytorch", "神经网络", "图像识别", "人脸识别",
            "自然语言处理", "机器人", "自动驾驶", "chatgpt", "openai",
            "prompt", "llm", "llama", "claude", "generative-ai")),
    TOOL("工具", List.of("编辑器", "git", "github", "visual-studio-code", "visual-studio",
            "intellij-idea", "sublime-text", "webstorm", "pycharm", "goland",
            "phpstorm", "vim", "emacs")),
    OTHER("其他", List.of("程序员", "segmentfault", "restful", "graphql", "安全",
            "xss", "csrf", "rpc", "http", "https",
            "udp", "websocket", "比特币", "以太坊", "智能合约",
            "区块链", "leetcode"));

    private final String category;
    private final List<String> tags;

    CategoryEnum(String category, List<String> tags) {
        this.category = category;
        this.tags = tags;
    }

    public String getName() {
        return category;
    }

    public List<String> getTags() {
        return tags;
    }
    }
