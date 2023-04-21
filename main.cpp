#include <QGuiApplication>
#include <QQmlApplicationEngine>
#include <QJniObject>
#include "contactmodel.h"


int main(int argc, char *argv[])
{
    QGuiApplication app(argc, argv);

    //QJniObject javaClass = QNativeInterface::QAndroidApplication::context();

    qmlRegisterType<ContactModel>("contact", 1, 0, "ContactModel");

    //javaClass.callMethod<void>("printSmth");


    //int numba = (int) javaClass.callMethod<jint>("rtrnSmth");

    //qDebug()<< numba;


    QQmlApplicationEngine engine;
    const QUrl url(u"qrc:/Contacts/Main.qml"_qs);
    QObject::connect(&engine, &QQmlApplicationEngine::objectCreationFailed,
        &app, []() { QCoreApplication::exit(-1); },
        Qt::QueuedConnection);
    engine.load(url);

    return app.exec();
}
