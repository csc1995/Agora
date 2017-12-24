const {getCollection, collectionNames, generateNextId} = require('../db')
const collection = () => getCollection(collectionNames.proposals)
const getNextId = () => generateNextId(collectionNames.proposals)

async function create({username, title, content, location, zone, categoria}) {
    const object = {
        id: await getNextId(),
        owner: username,
        title: title,
        categoria: categoria,
        content: content,
        createdDateTime: new Date(),
        updatedDateTime: null,
        comments: [],
        zone: zone,
        location: {
            lat: null,
            long: null
        }
    }
    if (location && location.lat && location.long) {
        object.location.lat = location.lat
        object.location.long = location.long
    }

    return collection().insertOne(object)
}

async function getAllBy(reqQuery, reqSort) {

    const query = {}, sort = {}

    if (reqQuery.username) {
        query.owner = reqQuery.username.toString()
    }

    if (reqQuery.category !== undefined) {
        query.categoria = reqQuery.category.toString()
    }

    if (reqQuery.zone !== undefined) {
        query.zone = parseInt(reqQuery.zone)
    }

    if (reqQuery.favorites) {
        query.id = {$in : reqQuery.favorites}
    }

    if (sort.createdDateTime) {
        sort.createdDateTime = reqSort.createdDateTime
    }

    const cursor = collection().find(query)

    if (Object.keys(reqSort).length > 0) {
        return cursor.sort(sort).toArray()
    } else {
        return cursor.toArray()
    }
}

async function getByUsername({username}) {
    return collection().find({owner: username}, {_id: 0}).toArray()
}

async function getProposalById({id, zone}) {
    const query = {
        id: parseInt(id)
    }
    if (zone !== undefined) {
        query.zone = parseInt(zone)
    }
    return collection().findOne(query, {_id: 0})
}

async function update({id, content, title, location}) {
    const query = {
        id: parseInt(id)
    }

    const update = {
        $set: {
            updatedDateTime: new Date()
        }
    }

    const options = {
        upsert: false,
        returnOriginal: false
    }

    if (content) {
        update.$set.content = content
    }

    if (title) {
        update.$set.title = title
    }

    if (location && location.lat && location.long) {
        update.$set.location = {
            lat: location.lat,
            long: location.long
        }
    }

    return collection().findOneAndUpdate(query, update, options)
        .then(response => response.value)
}

async function deleteProposal({id}) {
    return collection().deleteOne({id: parseInt(id)})
}

async function addComment({proposalId, author, comment}) {
    const query = {
        id: parseInt(proposalId),
    }

    const update = {
        $push: {
            comments: {
                id: await generateNextId('comment'),
                createdDateTime: new Date(),
                updatedDateTime: null,
                author: {
                    id: parseInt(author.id),
                    username: author.username.toString()
                },
                comment: comment.toString()
            }
        }
    }

    const options = {
        upsert: false,
        returnOriginal: false
    }

    return collection().findOneAndUpdate(query, update, options)
        .then(response => response.value)
}

async function deleteComment({proposalId, author, commentId}) {
    const query = {
        id: parseInt(proposalId),
        "comments.id": parseInt(commentId),
        "comments.author.username": author.toString()
    }

    const update = {
        $pull: {
            comments: {
                id: parseInt(commentId),
            }
        }
    }

    const options = {
        multi: true
    }

    return collection().update(query, update, options)
        .then(response => response.value)
}

async function editComment({proposalId, author, commentId, comment}) {
    const query = {
        id: parseInt(proposalId),
        'comments.id': parseInt(commentId),
        'comments.author.username': author.toString()
    }

    const update = {
        $set: {
            'comments.$.comment': comment,
            'comments.$.updatedDateTime': new Date()
        }
    }


    return collection().update(query, update)
        .then(response => response.value)
}

async function addImage({proposalId, images}) {
    const query = {
        id: parseInt(proposalId),
    }

    console.log('images', JSON.stringify(images, null, 4), typeof images)

    for (let i = 0; i < images.length; ++i) {
        images [i] = {
            image: images[i],
            id: await generateNextId('image')
        }
    }

    const update = {
        $pushAll: {
            images
        }
    }
    console.log(update)
    const options = {
        upsert: false,
        returnOriginal: false
    }

    return collection().findOneAndUpdate(query, update, options)
        .then(response => response.value)
}

async function deleteImage({proposalId, username,imageId}) {
    const query = {
        id: parseInt(proposalId),
        "images.id": parseInt(imageId)
    }

    const update = {
        $pull: {
            images: {
                id: parseInt(imageId)
            }
        }
    }

    console.log('query', query, 'upd', update)

    const options = {
        multi: true
    }

    return collection().update(query, update, options)
        .then(response => response.value)
}

module.exports = {
    create: create,
    update: update,
    getAllBy: getAllBy,
    getByUsername: getByUsername,
    getProposalById: getProposalById,
    addComment: addComment,
    editComment: editComment,
    deleteComment: deleteComment,
    delete: deleteProposal,
    addImage: addImage,
    deleteImage: deleteImage,
}