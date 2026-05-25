

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



далее создалиактивити и макеты для логина, регистрации, сброса пароля и страницы Users


!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
При вводе в поля на маленьких телефонах, клавиатура может перекрывать поле ввода.
Чтобы этого небыло, нужно в AndroidManifest.xml
в каждой активити где это может случиться добавить строку
    android:windowSoftInputMode="adjustResize"
        Напр:
            <activity
                android:windowSoftInputMode="adjustResize"
                android:name=".RegistrationActivity"
                android:exported="false" />
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!




!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
В прошлых проектах классы ViewModel наследовались от AndroidViewModel
    LoginViewModel extends AndroidViewModel
это нужно когда используется контекст - например, для работы с бд и тд
если этого нет, можно наследоваться от ViewModel
    LoginVimodel extends ViewModel
тогда и конструктор по сути переопределять не обязательно
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!




Сделали все что связано с логином:
    - login activity
    - registration activity
    - reset password activity
    - макет страницы со списком пользователей (типа вошли в систему)
настроили логику и отображение.
Ничего нового тут нет, потому без описания


Потом сделали для отображения списка пользователей (Users activity):
    - RecyclerView,
    - adapter,
    - user_item
тоже ничего нового





**************************************      БД Firebase
При регистрации пользователя создается запись в бд с id, email, имя и тд
документация - build - Firebase Realtime Database - android - get started
консоль - realtime database - create.. - united states - test mode

дальше документация рекомендует настроить правили доступа, но препод решил этого не делать.

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); // база данных
    private DatabaseReference databaseReference = firebaseDatabase.getReference("Messages");    // Messages - таблица в бд. если нет - создается
    databaseReference.setValue("My second zapis");      - внести запсь в бд в таблицу Messages
            {
              "Messages": "My second zapis"
            }
метод   .setValue   перезапишет старые данные новыми
Если нужно не перезаписать весь Messages, а добавить новое сообщение — обычно используют push():
    databaseReference.push().setValue("Новое сообщение");
Тогда получится:
    {
      "Messages": {
        -OtTp20VNyGr9R5lSqEq: "My fird zapis"
        -OtTp8X3HocMmdcPsPlp: "My четвертый zapis"
      }
    }
push() создает уникальный id для каждой записи.

        запись в бд
        for (int i=0; i<10; i++) {
            String zapis = String.format("My %d zapis", i);
            databaseReference.push().setValue(zapis);
        }
        // чтение из бд
        databaseReference.addValueEventListener(new ValueEventListener() {      // чтоб читать надо повесить слушатель
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {          // слушатель на изменение записи
                    // onDataChange вызывается когда происходит изменение в таблицк Messages     (databaseReference = firebaseDatabase.getReference("Messages");)
                    // т.е. при изменении таблицы Messages вызывается onDataChange, в который прилетает вся таблица Messages в обновленном виде  - snapshot

                for ( DataSnapshot dataSnapshot : snapshot.getChildren() ) {    // snapshot.getChildren() - список всех записей в таблице Messages
                        // dataSnapshot - одна запись в таблице Messages
                    String message = dataSnapshot.getValue(String.class);   // String.class - тип записи в таблице
                    Log.d("UsersActivity", message);
                }
            };

            @Override
            public void onCancelled(@NonNull DatabaseError error) {         // слушатель на ошибку

            }
        });

В таблицу можно вносить объекты:
        for (int i=0; i<10; i++) {
            User user = new User("id " + i , "name " + i, "last name " + i, i, true);
            databaseReference.push().setValue(user);
        }





