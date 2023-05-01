
#ifndef CONTACTMODEL_H
#define CONTACTMODEL_H

#include <QAbstractListModel>
#include <qjniobject.h>



class ContactModel : public QAbstractListModel
{
    Q_OBJECT

public:
    explicit ContactModel(QObject *parent = nullptr);

    enum{
        Name,
        Number
    };

    void addNewContact(int, QString);
    void removeContact(int);

    void updateContactList(const QStringList &updatedList);

    // Basic functionality:
    int rowCount(const QModelIndex &parent = QModelIndex()) const override;

    QVariant data(const QModelIndex &index, int role = Qt::DisplayRole) const override;

    // Editable:
    bool setData(const QModelIndex &index, const QVariant &value,
                 int role = Qt::EditRole) override;

    Qt::ItemFlags flags(const QModelIndex& index) const override;

    virtual QHash<int, QByteArray> roleNames() const override;

    QJniObject getArrayList() const;
    void setArrayList(const QJniObject &newArrayList);

    QList<QString> getContactList() const;
    void setContactList(const QList<QString> &newContactList);

signals:
    void arrayListChanged();

    void contactListChanged();

private:
    QJniObject arrayList;
    Q_PROPERTY(QJniObject arrayList READ getArrayList WRITE setArrayList NOTIFY arrayListChanged)
    QList<QString> contactList;
    Q_PROPERTY(QList<QString> contactList READ getContactList WRITE setContactList NOTIFY contactListChanged)
};

#endif // CONTACTMODEL_H
