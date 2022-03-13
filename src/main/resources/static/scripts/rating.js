$(function () {
    var val = [[${product.rating}]];
    $("#rateYo").rateYo({
        rating: val,
        readOnly: true
    });

});


// <script th:inline="javascript">
//     $(function () {
//     var val = [[${product.rating}]];
//     $("#rateYo").rateYo({
//     rating: val,
//     readOnly: true
// });
// });
// </script>