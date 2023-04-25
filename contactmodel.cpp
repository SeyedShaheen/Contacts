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

void ContactModel::updateContactList(const QStringList &updatedList)
{
    beginResetModel();
    // Clear the current arrayList
    arrayList.callMethod<void>("clear");
    // Insert the updated list into the arrayList
    for (const QString &contact : updatedList) {
        QJniObject javaString = QJniObject::fromString(contact);
        arrayList.callMethod<jboolean>("add", "(Ljava/lang/Object;)Z", javaString.object<jobject>());
    }
    endResetModel();
}

extern "C" {
JNIEXPORT void JNICALL
Java_com_example_myappication_MainActivity_update(JNIEnv *env, jobject, jobject updated, jlong ptr) {

    QJniObject arrList = updated;
    jlong len = arrList.callMethod<jint>("size", "()I");
    QStringList updatedList;


    for (jint i = 0; i < len; ++i) {
        QJniObject element = arrList.callObjectMethod("get", "(I)Ljava/lang/Object;", i);
        QString contact = QJniObject(element).toString();
        updatedList.append(contact);
        qDebug() << element.toString();
    }

    auto contactModel = reinterpret_cast<ContactModel*>(static_cast<long long>(ptr));
    if (contactModel) {
        contactModel->updateContactList(updatedList);
    }

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

    qDebug()<< qstring;

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

