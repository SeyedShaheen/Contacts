#include "contactmodel.h"

contactModel::contactModel(QObject *parent)
    : QAbstractItemModel(parent)
{
}

QVariant contactModel::headerData(int section, Qt::Orientation orientation, int role) const
{
    // FIXME: Implement me!
}

QModelIndex contactModel::index(int row, int column, const QModelIndex &parent) const
{
    // FIXME: Implement me!
}

QModelIndex contactModel::parent(const QModelIndex &index) const
{
    // FIXME: Implement me!
}

int contactModel::rowCount(const QModelIndex &parent) const
{
    if (!parent.isValid())
        return 0;

    // FIXME: Implement me!
}

int contactModel::columnCount(const QModelIndex &parent) const
{
    if (!parent.isValid())
        return 0;

    // FIXME: Implement me!
}

bool contactModel::hasChildren(const QModelIndex &parent) const
{
    // FIXME: Implement me!
}

bool contactModel::canFetchMore(const QModelIndex &parent) const
{
    // FIXME: Implement me!
    return false;
}

void contactModel::fetchMore(const QModelIndex &parent)
{
    // FIXME: Implement me!
}

QVariant contactModel::data(const QModelIndex &index, int role) const
{
    if (!index.isValid())
        return QVariant();

    // FIXME: Implement me!
    return QVariant();
}

QHash<int, QByteArray> contactModel::roleNames() const
{

}
