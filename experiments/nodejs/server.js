const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');

const app = express();
const PORT = 3000;

app.use(cors());
app.use(bodyParser.json());

let drives = [
    { id: 1, company: 'TCS', role: 'Software Developer', minCgpa: 7.0 },
    { id: 2, company: 'Infosys', role: 'Systems Engineer', minCgpa: 6.5 }
];

// GET all
app.get('/api/drives', (req, res) => {
    res.json(drives);
});

// GET by id
app.get('/api/drives/:id', (req, res) => {
    const drive = drives.find(d => d.id == req.params.id);
    drive ? res.json(drive) : res.status(404).send('Not Found');
});

// POST
app.post('/api/drives', (req, res) => {
    const newDrive = { id: Date.now(), ...req.body };
    drives.push(newDrive);
    res.status(201).json(newDrive);
});

// PUT
app.put('/api/drives/:id', (req, res) => {
    const idx = drives.findIndex(d => d.id == req.params.id);
    if (idx >= 0) {
        drives[idx] = { ...drives[idx], ...req.body };
        res.json(drives[idx]);
    } else {
        res.status(404).send('Not Found');
    }
});

// DELETE
app.delete('/api/drives/:id', (req, res) => {
    drives = drives.filter(d => d.id != req.params.id);
    res.status(204).send();
});

app.listen(PORT, () => {
    console.log(`Express REST API running on http://localhost:${PORT}`);
});
