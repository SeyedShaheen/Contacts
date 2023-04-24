
#ifndef CONTACTMODEL_H
#define CONTACTMODEL_H

#include <QAbstractListModel>
#include <qjniobject.h>



class ContactModel : public QAbstractListModel
{
    Q_OBJECT
    Q_PROPERTY(QString updateValue READ getUpdateValue WRITE setUpdateValue NOTIFY updateValueChanged)

public:
    explicit ContactModel(QObject *parent = nullptr);

    enum{
        Name,
        Number
    };

    QString updateValue;

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

    QString getUpdateValue() const;
    void setUpdateValue(const QString &newUpdateValue);

signals:
    void arrayListChanged();

    void updateValueChanged();

private:
    QJniObject arrayList;
    Q_PROPERTY(QJniObject arrayList READ getArrayList WRITE setArrayList NOTIFY arrayListChanged)
};

#endif // CONTACTMODEL_H
