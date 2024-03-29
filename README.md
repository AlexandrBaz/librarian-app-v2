

Не большое приложение на SpringBoot:

    SpringSecurity
    SpringData JPA + NaiveQuery
    Thymeleaf
    ModelMapper
    Lombok
    Hibernate.Validation
    Pagination and Sort
    Postgresql (для профиля application-prod.yml)
    H2database (для профиля application-dev.yml + data-h2.sql с профилями пользователей и книгами, для демонстрации)

по умолчанию используется профиль application-dev.yml вместе с H2 + data-h2.sql для демонстрации проекта. При включении профиля application-prod.yml рекомендуется закомментировать зависимость H2 в pomxml во избежание конфликтов

Роль Админ

    Краткая статистика по библиотеке (Пользователи \ Менеджеры \ Книги \ Невозвращенные книги \ Забаненные пользователи)
    Информация о пользователях (ФИО, Email, Кол-во книг на руках, Статус, Профиль)
    Профиль пользователя отражает список всех его книг, также позволяет забанить\разбанить
    Страница "Список менеджеров" - выводить список всех менеджеров, также позволяет забанить\разбанить
    Страница "Добавить менеджера" - добавляет нового менеджера
    Страница "Профиль" - позволяет изменить данные админа (использует Валидацию, при смене пароля используется проверка)
    Логин\Пароль для админа admin/admin (* при использовании профиля application-dev.yml)

Роль Менеджер

    Статистика по должникам (ФИО \ Название Книги \ Дата возврата книги (количество дней зависит от профиля в dev 10, в prod 30) \ Кол-во дней на которое просрочена книга \ Профиль пользователя)
    Профиль пользователя отражает ФИО \ Email \ Статус, а также список всех его книг. Пользователя можно забанить\разбанить
    Пользователи - отражает список всех пользователей, количество книг на руках, а также их статусы. Возможно отрыть профиль пользователя
    Книги - список всех книг (Название, Имя автора, Год выпуска, Кол-во книг на остатке) - есть поиск по названию книги(contains), также реализованна пагинация и сортировка (asc/desc по столбцам Название, Имя автора, Год выпуска, Кол-во книг на остатке)
    Страница "Добавить книгу" - позволяет добавить книгу в библиотеку (использует Валидацию)
    Страница "Профиль" - позволяет изменить данные менеджера (использует Валидацию, при смене пароля используется проверка)
    Логин\Пароль для менеджеров manager/manager, manager2/manager2 (* при использовании профиля application-dev.yml)

Роль пользователь

    Страница "Количество взятых книг" - показывает количество книг на руках, а также информацию по ним (Название книги \ Имя автора \ Год выпуска \ Дата возврата книги). Позволяет вернуть книгу обратно. Красным выделены книги по которым срок возврата прошел
    Страница "Список доступных книг" - отображает список книг которые пользователь может взять, уже взятые книги в список не попадают. Реализован поиск по названию, пагинация и сортировка (asc/desc по столбцам Название, Имя автора, Год выпуска)
    Страница "Профиль" - позволяет изменить данные пользователя (использует Валидацию, при смене пароля используется проверка)
    Логин\Пароль для менеджеров user/user, user2/user2, user3/user3, banned/banned (* при использовании профиля application-dev.yml)
