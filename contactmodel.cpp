#include <QGuiApplication>
#include <QQmlApplicationEngine>
#include <QJniObject>
#include <jni.h>
#include "contactmodel.h"



ContactModel::ContactModel(QObject *parent)
    : QAbstractListModel(parent)
{
    QJniObject javaClass = QNativeInterface::QAndroidApplication::context();
    arrayList = javaClass.callObjectMethod("readContacts", "()Ljava/util/ArrayList;");
    javaClass.callMethod<void>("setPointer", "(J)V", (long long) (ContactModel*) this);
}

void ContactModel::addNewContact(int index, QString value)
{
    QJniObject javaString = QJniObject::fromString(value);

    // create a new ArrayList and add elements in desired order
    QJniObject newArrayList("java/util/ArrayList");
    for (int i = 0; i < index; i++) {
        QJniObject element = arrayList.callObjectMethod("get", "(I)Ljava/lang/Object;", i);
        newArrayList.callMethod<jboolean>("add", "(Ljava/lang/Object;)Z", element.object());
    }
    newArrayList.callMethod<jboolean>("add", "(Ljava/lang/Object;)Z", javaString.object());
    for (int i = index; i < arrayList.callMethod<jint>("size","()I"); i++) {
        QJniObject element = arrayList.callObjectMethod("get", "(I)Ljava/lang/Object;", i);
        newArrayList.callMethod<jboolean>("add", "(Ljava/lang/Object;)Z", element.object());
    }

    // replace existing list with new list
    beginResetModel();
    arrayList = newArrayList;
    endResetModel();
}

void ContactModel::removeContact(int index)
{
    beginRemoveRows(QModelIndex(), index,index);
    arrayList.callObjectMethod("remove", "(I)Ljava/lang/Object;", index);
    endRemoveRows();
}

extern "C" {
JNIEXPORT void JNICALL
Java_com_example_myappication_MainActivity_update(JNIEnv *env, jobject, jlong ptr, jstring elem, jint index) {

    const char* chars = env->GetStringUTFChars(elem, nullptr);
    if (chars){
        QString qStr = QString::fromUtf8(chars);
        env->ReleaseStringUTFChars(elem, chars);
        ContactModel* model = reinterpret_cast<ContactModel*>(ptr);
        model->addNewContact(index,qStr);
    }
}
}

extern "C" {
JNIEXPORT void JNICALL
Java_com_example_myappication_MainActivity_removeFromModel(JNIEnv *env, jobject, jlong ptr, jint index) {
    qDebug() << index;
    ContactModel* model = reinterpret_cast<ContactModel*>(ptr);
    model->removeContact(index);
}
}


int ContactModel::rowCount(const QModelIndex &parent) const
{
    // For list models only the root node (an invalid parent) should return the list's size. For all
    // other (valid) parents, rowCount() should return 0 so that it does not become a tree model.
    if (parent.isValid())
        return 0;

    // FIXME: Implement me!
    return (int)arrayList.callMethod<jint>("size","()I");
}

QVariant ContactModel::data(const QModelIndex &index, int role) const
{
    if (!index.isValid())
        return QVariant();

    // FIXME: Implement me!
    QJniObject element = arrayList.callObjectMethod("get", "(I)Ljava/lang/Object;", index);
    QString qstring = element.toString();
    QStringList contactInfo = qstring.split(":");

    //qDebug()<< qstring;

    switch (role){
    case Name:
        return QVariant((contactInfo[0]));
    case Number:
        return QVariant((contactInfo[1]));
    }
     return QVariant();
}

bool ContactModel::setData(const QModelIndex &index, const QVariant &value, int role)
{
    if (data(index, role) != value) {
        // FIXME: Implement me!
        emit dataChanged(index, index, {role});
        return true;
    }
    return false;
}

Qt::ItemFlags ContactModel::flags(const QModelIndex &index) const
{
    if (!index.isValid())
        return Qt::NoItemFlags;

    return QAbstractItemModel::flags(index) | Qt::ItemIsEditable; // FIXME: Implement me!
}

QHash<int, QByteArray> ContactModel::roleNames() const
{
    QHash<int,QByteArray> contacts;
    contacts[Name] = "name";
    contacts[Number] = "number";
    return contacts;
}

QJniObject ContactModel::getArrayList() const
{
    return arrayList;
}

void ContactModel::setArrayList(const QJniObject &newArrayList)
{
    if (arrayList == newArrayList)
        return;
    arrayList = newArrayList;
    emit arrayListChanged();
}

