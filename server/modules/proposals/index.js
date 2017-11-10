const db = require('./proposals.db')
const userModule = require('../user')

async function createProposal({username, title, content}) {
    const existingUser = await userModule.get({username})
    if (!existingUser) {
        throw new Error(`User does not exists`)
    }
    return await db.create({username, title, content})
}

async function getAllProposals() {
    const proposals = await db.getAll();
    if (!proposals) {
        throw new Error(`No Proposals`)
    }
    return proposals
}

async function getProposalsByUsername({username}) {
    const proposals = await db.getByUsername({username});
    if (!proposals) {
        throw new Error(`No Proposals`)
    }
    return proposals
}

module.exports = {
    createProposal: createProposal,
    getAllProposals: getAllProposals,
    update: db.update,
    deleteProposal: db.delete,
    getProposalsByUsername: getProposalsByUsername,
    getProposalById: db.getProposalById,
}