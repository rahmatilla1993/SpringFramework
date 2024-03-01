const select = document.querySelector('#setLang')
select.addEventListener('change', (event) => {
    event.preventDefault()
    const params = new URLSearchParams(window.location.search)
    params.set('lang', event.target.value)
    window.location.search = params.toString()
})