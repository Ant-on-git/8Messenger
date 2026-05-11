

**************************************      Firebase
Firebase позволяет фронтенд-разработчикам создавать полноценные приложения без необходимости писать собственный бэкенд с нуля.
Почти все в Firebase выполняется во вспомогательном потоке.
Большинство методов соединяются с сетью\бд потому под капотом автоматом вызывается переключение потоков там, где это необъодимо.

Функционал:
    - авторизация
    - база данных
    - отправка сообщений
    - хранилище

в этом прил. будем исп. авторизаци  и бд

заходим на https://firebase.google.com/
        getstarted
        создаем проект - отключаем аналитику, тк тут не будем использовать
документация:
    правое меню - help - firebase developer docs
смотрим про аутентификацию:
    строить (build) - authentication    - android - getstarted
    -> Если вы еще этого не сделали, добавьте Firebase в свой Android-проект.
    имя проекта:
        build.gradle - applicationId
делаем все по инструкции. добавляем файл с настройками, указываем плагины и зависимости, синзронизируем.. и готово
добавляем аутентификацию:
    implementation(platform("com.google.firebase:firebase-bom:34.13.0"))
    implementation("com.google.firebase:firebase-auth")


!!! ПС1 Практически все в Firebase делается через  Firebase__.getInstance();
!!! ПС2 все методы интуитивно понятны по названию

проверяем состояние + создаем польз:
    private FirebaseAuth mAuth;
        activity
            onCreate
                auth = FirebaseAuth.getInstance();
                // создаем пользователя
                auth.createUserWithEmailAndPassword("test_email@mail.ru", "test_password")
                        .addOnSuccessListener(authResult -> {})
                        .addOnFailureListener(exception -> {});

!!! здесь используем метод через Email и Password
чтобы это  работало, надо на сайте firebase зайти в консоль приложения (этого приложения)
и активировать нужный метод. напр:
    firebase - 8Messenger - authentication - Sign-in method - email\Password - enable
там есть еще пункт     Email link (passwordless sign-in)    это для входа на почту приходят разовые ссылки и польз авторизуется по ним

Итого создали польз с данными для входва   test_email@mail.ru   \    test_password
если запустить код 2 раза - выйдет ошибка что польз с таким Email существует

Посмотреть всех пользователей:
    firebase - 8Messenger - authentication - users

создаем польз с почтой и паролем пользователя
        auth.createUserWithEmailAndPassword("test_email@mail.ru", "test_password")
                .addOnSuccessListener(authResult -> {})
                .addOnFailureListener(exception -> {});

войти в систему с почтой и паролем
        auth.signInWithEmailAndPassword("test_email@mail.ru", "test_password")
                .addOnSuccessListener(authRes -> {
                    FirebaseUser currentUser = auth.getCurrentUser();
                    Log.d("MainActivity", currentUser.getEmail());
                })
                .addOnFailureListener(exception -> {Log.d("MainActivity", exception.getMessage());});

выйти
    auth.signOut();
повесить слушателя на изменение состояния авторизации
        auth.addAuthStateListener(firebaseAuth -> {
            if(auth.getCurrentUser() != null){
                Log.d("MainActivity", "польз авторизован");
            } else {
                Log.d("MainActivity", "польз не авторизован");
            }
        });

отпр ссылку для сброса пароля на почту
        auth.sendPasswordResetEmail( currentUser.getEmail() )
                .addOnSuccessListener( Void -> Log.d("MainActivity", "ссылка для сброса отправлена") )
                .addOnFailureListener( exc -> Log.d("MainActivity", exc.getMessage()) );

Верификация: отправить пароль на почту
    currentUser.sendEmailVerification();

Верификация: почта подтверждена?  (boolean)
    currentUser.isEmailVerified();





