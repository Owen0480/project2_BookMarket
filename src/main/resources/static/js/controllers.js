function addToCart(bookid) {
    if (confirm("장바구니에 도서를 추가합니까?")) {
        // FormData 생성
        var formData = new FormData();
        formData.append('_method', 'put');

        // fetch로 PUT 요청
        fetch('/BookMarket/cart/book/' + bookid, {
            method: 'POST',
            body: formData
        })
        .then(response => {
            if (response.status === 204 || response.ok) {
                // 성공 시 장바구니 페이지로 이동
                window.location.href = '/BookMarket/cart';
            } else {
                alert('장바구니 추가에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('오류가 발생했습니다.');
        });
    }
}

function removeFromCart(bookid, cartId) {
document.removeForm.action = "/BookMarket/cart/book/"+bookid;
document.removeForm.submit();
setTimeout('location.reload()',10);
}
function clearCart(cartId) {
if (confirm("모든 도서를 장바구니에서 삭제합니까?") == true) {
    document.clearForm.submit();
    setTimeout('location.reload()',10);
    }
}