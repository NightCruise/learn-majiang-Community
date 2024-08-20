// 添加标签
document.addEventListener('DOMContentLoaded', function () {
    console.log(1)
    const tagsContainer = document.getElementById('tagsContainer');
    const tagInput = document.getElementById('tagInput');
    const addTagButton = document.getElementById('addTagButton');
    const tagsInput = document.getElementById('tagsInput');

    // Array to keep track of tag indices
    let tagIndices = [];

    // Function to add a tag
    function addTag(tagText) {
        if (tagsContainer.children.length >= 2) {
            alert('最多只能添加两个标签');
            return;
        }

        const tagIndex = tagIndices.length > 0 ? Math.max(...tagIndices) + 1 : 0;
        console.log(tagIndices);
        tagIndices.push(tagIndex);

        // Create a new span element for the tag
        const tagh4 = document.createElement('h4')
        const tagSpan = document.createElement('span');
        tagh4.className = 'h4-tag'
        tagSpan.className = 'label label-info new-tag';
        tagSpan.textContent = tagText;
        tagSpan.classList.add(`tag-label-${tagIndex}`); // Add unique class

        // Create a delete button
        const deleteButton = document.createElement('button');
        deleteButton.className = 'btn btn-danger btn-xs ml-2';
        deleteButton.textContent = '×';
        deleteButton.style.display = 'none'; // Initially hidden
        deleteButton.addEventListener('click', function () {
            // Remove the tag from tagIndices
            // tagIndices = tagIndices.filter(index => index !== tagIndex);

            // Find the tag with the corresponding class and remove it
            const tagToRemove = document.querySelector(`.tag-label-${tagIndex}`);
            if (tagToRemove) {
                tagsContainer.removeChild(tagToRemove.parentElement); // Remove the <h4> element
            }
            updateTagsInput(); // Update hidden input field
        });

        // Append the delete button to the tag
        tagSpan.appendChild(deleteButton);

        // Show delete button on hover
        tagSpan.addEventListener('mouseenter', function () {
            deleteButton.style.display = 'inline';
        });
        tagSpan.addEventListener('mouseleave', function () {
            deleteButton.style.display = 'none';
        });

        tagh4.appendChild(tagSpan);
        tagsContainer.appendChild(tagh4)

        // Update the hidden input field with the tag values
        updateTagsInput();
    }

    // Function to update hidden input field with current tags
    function updateTagsInput() {
        const tags = Array.from(tagsContainer.querySelectorAll('.label')).map(tag => tag.textContent);
        tagsInput.value = tags.join(',');
    }

    // Add tag on button click
    addTagButton.addEventListener('click', function () {
        const tagText = tagInput.value.trim();
        if (tagText) {
            addTag(tagText);
            tagInput.value = ''; // Clear the input field
        }
    });

    // Optionally, allow pressing Enter to add tags
    tagInput.addEventListener('keypress', function (e) {
        if (e.key === 'Enter') {
            e.preventDefault();
            addTagButton.click();
        }
    });
});

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