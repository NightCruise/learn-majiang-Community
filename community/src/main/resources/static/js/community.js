/**
 * 根据type决定评论或者回复
 * @param targetId
 * @param content
 * @param type
 */
function comment2Target(targetId, content, type) {
    if (!content) {
        alert("不能回复空内容~~~");
        return;
    }
    $.ajax({
        url: "/comment",
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        type: "POST",
        dataType: "json",
        contentType: "application/json",
    })
        .done(function (response) {
            if (response.code === 200) {
                window.location.reload();
            } else {
                if (response.code === 2003) {
                    let isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=Ov23liijchVoPwBKVJUY&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", true);
                    }
                } else {
                    alert(response.message);
                }
            }
        })
        .fail(function (xhr, status, errorThrown) {
            alert("Sorry, there was a problem!");
            console.log("Error: " + errorThrown);
            console.log("Status: " + status);
            console.dir(xhr);
        })
}

function formatTimestamp(timestamp){
    localMoment();
    let momentTime = moment(timestamp);
    // 判断时间戳是否在一天之内
      // 超过一天，显示标准日期格式
    return moment().diff(momentTime, 'weeks') < 1
        ? momentTime.fromNow()  // 显示相对时间（如 "1 小时前"）
        : momentTime.format('YYYY-MM-DD');
}

/**
 * 转换二级评论的时间戳
 */
$(document).ready(function() {
    $('.comment-time').each(function() {
        let timestamp = $(this).data('timestamp');
        let formattedTime = formatTimestamp(timestamp)
        $(this).text(formattedTime);
    });
});

/**
 * 提交回复
 */
function comment() {
    let questionId = $("#question_id").val();
    let content = $("#comment_content").val();
    comment2Target(questionId, content, 1);
}

function reply(that) {
    let replyId = that.getAttribute("data-id");
    let content = $("#reply-" + replyId).val();
    comment2Target(replyId, content, 2);
}

/**
 * 展开二级评论
 */
function collapseComments(that) {
    let id = that.getAttribute("data-id");
    let comments = $("#comment-" + id);

    let collapse = that.getAttribute("data-collapse");

    if (collapse) {
        // 折叠二级评论
        comments.removeClass("in");
        that.removeAttribute("data-collapse");
        that.classList.remove("active");
    } else {
        let subCommentContainer = $('#comment-' + id);
        if (subCommentContainer.children().length !== 1) {
            // 展开二级评论
            comments.addClass("in");
            // 标记二级评论状态
            that.setAttribute("data-collapse", "in");
            that.classList.add("active");
        } else {
            $.getJSON("/comment/" + id, function (data) {
                $.each(data.data.reverse(), function (index, comment) {

                    let h5Element = $("<h5/>", {
                        "class": "media-heading",
                    }).append($("<a/>", {
                        "href": "#",
                        "text": comment.user.name != null ? comment.user.name : comment.user.login
                    }))

                    let menuElement = $("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "glyphicon glyphicon-thumbs-up icon"
                    })).append($("<span/>", {
                        "class": "glyphicon glyphicon-comment icon",
                        "data-id": comment.id,
                        "onclick": "replyOnSecondLevel(this)"
                    })).append($("<span/>", {
                        "class": "pull-right",
                        "text": formatTimestamp(comment.gmtCreate)
                    }))

                    let mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-thumbnail",
                        "src": comment.user.avatarUrl
                    }));

                    let mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append(h5Element)
                        .append($("<div/>", {
                            "text": comment.content,
                        })).append(menuElement);

                    let replyElement = $("<div/>", {
                        "class": "col-lg-12 col-sm-12 col-md-12 col-xs-12 collapse",
                        "id": 'reply-component-' + comment.id
                    }).append($("<textarea/>", {
                        "class": "form-control resizeTextarea",
                        "type": "text",
                        "placeholder": "请勿在评论里发布”+1，感谢”等信息",
                        "id": 'reply-' + comment.id,
                        "rows": "1"
                    })).append($("<button/>", {
                        "class": "btn btn-success pull-right",
                        "type": "button",
                        // "onclick": "reply(this)",
                        "id": comment.id
                    }).append("回复"))

                    let mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement)
                        .append(mediaBodyElement)
                        .append(replyElement);

                    let commentElement = $("<div/>", {
                        "class": "col-lg-12 col-sm-12 col-md-12 col-xs-12 comments",
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);

                });
            });
            // 展开二级评论
            comments.addClass("in");
            // 标记二级评论状态
            that.setAttribute("data-collapse", "in");
            that.classList.add("active");
        }
    }
}

/**
 * 回复二级评论
 */
function replyOnSecondLevel(that){
    let commentId = $(that).attr("data-id");
    $("#reply-component-" + commentId).toggleClass("col-lg-12 col-sm-12 col-md-12 col-xs-12 collapse in", "easeOutQuad")
}

/**
 * 本地Moment
 */
function localMoment() {
    moment.locale('zh-cn', {
        months: '一月_二月_三月_四月_五月_六月_七月_八月_九月_十月_十一月_十二月'.split('_'),
        monthsShort: '1月_2月_3月_4月_5月_6月_7月_8月_9月_10月_11月_12月'.split('_'),
        weekdays: '星期日_星期一_星期二_星期三_星期四_星期五_星期六'.split('_'),
        weekdaysShort: '周日_周一_周二_周三_周四_周五_周六'.split('_'),
        weekdaysMin: '日_一_二_三_四_五_六'.split('_'),
        longDateFormat: {
            LT: 'Ah点mm分',
            LTS: 'Ah点m分s秒',
            L: 'YYYY-MM-DD',
            LL: 'YYYY年MMMD日',
            LLL: 'YYYY年MMMD日Ah点mm分',
            LLLL: 'YYYY年MMMD日ddddAh点mm分',
            l: 'YYYY-MM-DD',
            ll: 'YYYY年MMMD日',
            lll: 'YYYY年MMMD日Ah点mm分',
            llll: 'YYYY年MMMD日ddddAh点mm分'
        },
        meridiemParse: /凌晨|早上|上午|中午|下午|晚上/,
        meridiemHour: function (hour, meridiem) {
            if (hour === 12) {
                hour = 0;
            }
            if (meridiem === '凌晨' || meridiem === '早上' ||
                meridiem === '上午') {
                return hour;
            } else if (meridiem === '下午' || meridiem === '晚上') {
                return hour + 12;
            } else {
                // '中午'
                return hour >= 11 ? hour : hour + 12;
            }
        },
        meridiem: function (hour, minute, isLower) {
            var hm = hour * 100 + minute;
            if (hm < 600) {
                return '凌晨';
            } else if (hm < 900) {
                return '早上';
            } else if (hm < 1130) {
                return '上午';
            } else if (hm < 1230) {
                return '中午';
            } else if (hm < 1800) {
                return '下午';
            } else {
                return '晚上';
            }
        },
        calendar: {
            sameDay: function () {
                return this.minutes() === 0 ? '[今天]Ah[点整]' : '[今天]LT';
            },
            nextDay: function () {
                return this.minutes() === 0 ? '[明天]Ah[点整]' : '[明天]LT';
            },
            lastDay: function () {
                return this.minutes() === 0 ? '[昨天]Ah[点整]' : '[昨天]LT';
            },
            nextWeek: function () {
                var startOfWeek, prefix;
                startOfWeek = moment().startOf('week');
                prefix = this.unix() - startOfWeek.unix() >= 7 * 24 * 3600 ? '[下]' : '[本]';
                return this.minutes() === 0 ? prefix + 'dddAh点整' : prefix + 'dddAh点mm';
            },
            lastWeek: function () {
                var startOfWeek, prefix;
                startOfWeek = moment().startOf('week');
                prefix = this.unix() < startOfWeek.unix() ? '[上]' : '[本]';
                return this.minutes() === 0 ? prefix + 'dddAh点整' : prefix + 'dddAh点mm';
            },
            sameElse: 'LL'
        },
        ordinalParse: /d{1,2}(日|月|周)/,
        ordinal: function (number, period) {
            switch (period) {
                case 'd':
                case 'D':
                case 'DDD':
                    return number + '日';
                case 'M':
                    return number + '月';
                case 'w':
                case 'W':
                    return number + '周';
                default:
                    return number;
            }
        },
        relativeTime: {
            future: '%s内',
            past: '%s前',
            s: '几秒',
            m: '1 分钟',
            mm: '%d 分钟',
            h: '1 小时',
            hh: '%d 小时',
            d: '1 天',
            dd: '%d 天',
            M: '1 个月',
            MM: '%d 个月',
            y: '1 年',
            yy: '%d 年'
        },
        week: {
            // GB/T 7408-1994《数据元和交换格式·信息交换·日期和时间表示法》与ISO 8601:1988等效
            dow: 1, // Monday is the first day of the week.
            doy: 4 // The week that contains Jan 4th is the first week of the year.
        }
    });
}

/**
 * 随内容增高的textarea
 */
document.addEventListener('input', function (event) {
    if (event.target.classList.contains('resizeTextarea')) {
        event.target.style.height = 'auto';
        event.target.style.height = (event.target.scrollHeight) + 'px';
    }
}, false);