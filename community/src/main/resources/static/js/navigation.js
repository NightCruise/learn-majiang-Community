$(document).ready(function() {
    $('#searchForm').submit(function(event) {
        let searchValue = $('#searchInput').val().trim();
        if (searchValue === '') {
            event.preventDefault();  // 阻止表单提交
            alert('请输入搜索内容');  // 提示用户输入
        }
    });
});