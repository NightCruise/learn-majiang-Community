// 对警告框的滚动监听
document.addEventListener('scroll', function () {
    const alertDiv = document.getElementById('alertDiv');
    const nav = document.querySelector('nav');
    const navBottom = nav.getBoundingClientRect().bottom;

    if (navBottom <= 0) {
        // 当导航栏滚动出视图时，将警告框固定在页面顶部
        alertDiv.style.top = '0';
    } else {
        // 当导航栏在视图中时，警告框跟随导航栏位置
        alertDiv.style.top = navBottom + 'px';
    }
});

// 当发布错误时候弹出警告框，3s后收回
document.addEventListener('DOMContentLoaded', function () {
    // document.querySelector('.btn-publish').addEventListener('click', function (event) {
    //     event.preventDefault();
    //
    //     // Show the alert with slide down animation
    //     const alertDiv = document.getElementById('alertDiv');
    //     alertDiv.style.display = 'block';
    //     alertDiv.classList.add('alert-slide-down');
    //
    //     // Hide the alert after 3 seconds with slide up animation
    //     setTimeout(function () {
    //         alertDiv.classList.remove('alert-slide-down');
    //         alertDiv.classList.add('alert-slide-up');
    //
    //         // After animation ends, completely hide the alert
    //         alertDiv.addEventListener('animationend', function () {
    //             alertDiv.style.display = 'none';
    //             alertDiv.classList.remove('alert-slide-up');
    //         }, { once: true });
    //     }, 3000); // 3秒后隐藏
    // });
});

/**
 * 点击标签按钮，选择相应的标签
 * @param that
 */
function selectTag(that) {
    let value = $(that).attr("data-tag");
    let previous = $("#tagInput").val()
    let tags = previous ? previous.split(',') : [];

    if (!tags.includes(value)) {
        if (previous) {
            $("#tagInput").val(previous + ',' + value);
        } else {
            $("#tagInput").val(value);
        }
        addTagDisplay(value);
    }
}

/**
 * 显示标签选择框
 */
function showSelectTag() {
    if ($("#select-tag").attr("style") && ($("#select-tag").attr("style").indexOf("display: none;")) === -1) {
        $("#select-tag").hide();
    } else {
        $("#select-tag").show();
    }
}

$(document).ready(function () {
    $("#select-tag").on("mousedown", function () {
        const tagTab = $(this);
        console.log(tagTab);
    });
});

/**
 * 在标签选择框点击其他地方失去焦点，选择框消失
 */
$(document).ready(function () {
    // 点击页面任何地方
    $(document).mousedown(function (event) {
        let $target = $(event.target);
        let $selectTag = $("#select-tag");
        let $addTagButton = $("#addTagButton");
        let $questionTag = $(".question-tag");

        // 检查点击是否在 #select-tag 之外，且不是点击在 #addTagButton 上
        if (!$target.closest('#select-tag').length && !$target.is($addTagButton) && !$target.is($questionTag)) {
            if ($selectTag.is(":visible")) {
                showSelectTag();  // 这里会调用方法来隐藏
            }
        }
    });
});

/**
 * 添加标签按钮在“+添加标签”按钮的左边
 * @param tag
 */
function addTagDisplay(tag) {
    let newTagButton = $('<button/>', {
        class: 'btn btn-primary question-tag',
        type: 'button',
        text: tag + ' ×',
        click: function () {
            removeTagDisplay(tag);
        }
    }).insertBefore('.addTagBtn');

}

/**
 * 去除在“+添加标签”按钮的左边的标签按钮
 * @param tag
 */
function removeTagDisplay(tag) {
    let tags = $("#tagInput").val().split(',');
    let index = tags.indexOf(tag);

    if (index > -1) {
        tags.splice(index, 1);  // 从数组中移除该标签
        $("#tagInput").val(tags.join(','));  // 更新 input 框

        // 删除该标签按钮
        $(".question-tag").filter(function () {
            return $(this).text() === tag + ' ×';
        }).remove();
    }
}