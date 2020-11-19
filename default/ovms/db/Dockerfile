FROM mysql:8.0.19

MAINTAINER ovms(wangiegie@gmail.com)

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY ./1schema.sql /docker-entrypoint-initdb.d

COPY ./2ovmsx.sql /docker-entrypoint-initdb.d

COPY ./3ovmsx_ac.sql /docker-entrypoint-initdb.d

COPY ./4ovmsx_job.sql /docker-entrypoint-initdb.d

COPY ./5ovmsx_mp.sql /docker-entrypoint-initdb.d

COPY ./6ovmsx_config.sql /docker-entrypoint-initdb.d

COPY ./7ovmsx_pay.sql /docker-entrypoint-initdb.d

COPY ./8ovmsx_codegen.sql /docker-entrypoint-initdb.d