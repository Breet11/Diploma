<template>
  <header class="header">
    <div class="header__inner">
      <RouterLink class="header__logo" to="/">Diploma Cars</RouterLink>
      <nav class="header__nav">
        <RouterLink to="/cars" class="header__link" active-class="header__link--active">
          Список автомобилей
        </RouterLink>
      </nav>
    </div>
  </header>
</template>

