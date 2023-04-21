
#ifndef CONTACTMODEL_H
#define CONTACTMODEL_H

#include <QAbstractListModel>



class ContactModel : public QAbstractListModel
{
    Q_OBJECT

public:
    explicit ContactModel(QObject *parent = nullptr);

    enum{
        Name,
        Number
    };

    // Basic functionality:
    int rowCount(const QModelIndex &parent = QModelIndex()) const override;

    QVariant data(const QModelIndex &index, int role = Qt::DisplayRole) const override;

    // Editable:
    bool setData(const QModelIndex &index, const QVariant &value,
                 int role = Qt::EditRole) override;

    Qt::ItemFlags flags(const QModelIndex& index) const override;

    virtual QHash<int, QByteArray> roleNames() const override;

private:
};

#endif // CONTACTMODEL_H
