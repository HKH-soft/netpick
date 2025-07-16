import { Form, Formik, useField} from "formik";
import { XCircleIcon } from '@heroicons/react/20/solid';
import { ChevronDownIcon } from '@heroicons/react/16/solid'
import { useEffect , useState } from "react"
import * as Yup from 'yup';
import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";

const MyTextInput = ({ label, ...props }) => {
// useField() returns [formik.getFieldProps(), formik.getFieldMeta()]
// which we can spread on <input>. We can use field meta to show an error
// message if the field is invalid and it has been touched (i.e. visited)
const [field, meta] = useField(props);
return (
    <>
    <div className={`sm:col-span-${props.gridsize}`}>
        <label htmlFor={props.id || props.name} className="block text-sm/6 font-medium text-white-900">
            {label}
        </label>
        <div className="mt-2">
            <input
            {...field} {...props}
            className="text-input block w-full rounded-md bg-white px-3 py-1.5 text-base text-zinc-900 outline-1 -outline-offset-1 outline-zinc-300 placeholder:text-zinc-400 focus:outline-2 focus:-outline-offset-2 focus:outline-emerald-600 sm:text-sm/6"
            />
        </div>
    {meta.touched && meta.error ? (
        <>
        <div className="rounded-md bg-red-50 p-2 mt-1 error">
            <div className="flex">
                <div className="shrink-0">
                    <XCircleIcon aria-hidden="true" className="size-5 text-red-400" />
                </div>
                <div className="ml-3">
                    <h3 className="text-sm font-medium text-red-800">{meta.error}</h3>
                </div>
            </div>
        </div>
        </>
    ) : null}
    </div>
    </>
);
};

const MySelect = ({ label, ...props }) => {
const [field, meta] = useField(props);
return (
    <div className={`sm:col-span-${props.gridsize}`}>
        <div>
            <label htmlFor={props.id || props.name} className="block text-sm/6 font-medium text-white-900">
                {label}
            </label>
            <div className="mt-2 grid grid-cols-1">
                <select
                {...field} {...props}
                className="col-start-1 row-start-1 w-full appearance-none rounded-md bg-white py-1.5 pr-8 pl-3 text-base text-zinc-900 outline-1 -outline-offset-1 outline-zinc-300 focus:outline-2 focus:-outline-offset-2 focus:outline-emerald-600 sm:text-sm/6"
                >
                </select>
                <ChevronDownIcon
                aria-hidden="true"
                className="pointer-events-none col-start-1 row-start-1 mr-2 size-5 self-center justify-self-end text-zinc-500 sm:size-4"
                />
            </div>
        </div>
    {meta.touched && meta.error ? (
        <>
        <div className="rounded-md bg-red-50 p-2 mt-1 error">
            <div className="flex">
                <div className="shrink-0">
                    <XCircleIcon aria-hidden="true" className="size-5 text-red-400" />
                </div>
                <div className="ml-3">
                    <h3 className="text-sm font-medium text-red-800">{meta.error}</h3>
                </div>
            </div>
        </div>
        </>
    ) : null}
    </div>
);
};

const RegisterationForm = () => {
    const {register} = useAuth()
    const navigate = useNavigate()
    return (
        <Formik
                validateOnMount={true}
                initialValues={{
                firstName: '',
                lastName: '',
                email: '',
                password: '',
                age: '',
                gender: '', 
                }}
                validationSchema={Yup.object({
                firstName: Yup.string()
                    .max(15, 'Must be 15 characters or less')
                    .matches(/^[a-zA-Z\s]*$/, 'Name can only contain English letters and spaces')
                    .required('Required'),
                lastName: Yup.string()
                    .max(20, 'Must be 20 characters or less')
                    .matches(/^[a-zA-Z\s]*$/, 'Name can only contain English letters and spaces')
                    .required('Required'),
                email: Yup.string()
                    .email('Invalid email address')
                    .required('Required'),
                password:Yup.string()
                    .min(8,"Password cannot be shorter than 8 characters")
                    .max(30,"Password cannot be longer than 30 characters")
                    .required("Password is required"),
                gender: Yup.string()
                    .oneOf(
                    ['Female','Male'],
                    'Invalid Gender'
                    )
                    .required('Required'),
                age: Yup.number("Must be a Number")
                    .max(100,'Must be 100 years or less')
                    .min(16,'Must be 16 years or more')
                    .required('Required'),
                })}
                onSubmit={({ firstName, lastName, gender, ...rest }, { setSubmitting }) => {
                    const customer = rest;
                    customer.name = `${firstName.trim()} - ${lastName.trim()}`;
                    customer.gender = gender === "Male" ? true : false ;
                    setSubmitting(true);
                    
                    register(customer)
                        .then(res => {
                            navigate("/dashboard")
                            console.log(res)
                        })
                        .catch(err => {
                            console.log(err)
                        })
                        .finally(() => {
                            setSubmitting(false);
                        })
                }}
            >
                {({isValid,isSubmitting,dirty}) => (
                    <Form
                        id='register'
                        className='grid max-w-2xl grid-cols-1 gap-x-6 gap-y-4 sm:grid-cols-6'
                    >
                    <MyTextInput
                        gridsize="3"
                        label="First Name"
                        name="firstName"
                        type="text"
                        placeholder="Jane"
                    />
        
                    <MyTextInput
                        gridsize="3"
                        label="Last Name"
                        name="lastName"
                        type="text"
                        placeholder="Doe"
                    />
        
                    <MyTextInput
                        gridsize="full"
                        label="Email Address"
                        name="email"
                        type="email"
                        placeholder="jane@formik.com"
                    />
        
                    <MyTextInput
                            gridsize="full"
                            label={"Password"}
                            name={"password"}
                            type={"password"}
                            placeholder={"Type your password"}
                    />
        
                    <MySelect label="Gender" name="gender" gridsize="4">
                        <option value="">Select Gender</option>
                        <option value="Female">Female</option>
                        <option value="Male">Male</option>
                    </MySelect>
        
                    <MyTextInput
                        gridsize="2"
                        label="Age"
                        name="age"
                        type="text"
                        placeholder="21"
                    />
                    
                    <button
                    type="submit"
                    form='register'
                    disabled={isSubmitting || !(dirty && isValid)}
                    className={`inline-flex w-full justify-center rounded-md px-3 py-2 text-sm font-semibold text-white shadow-xs sm:col-span-full ${
                        isSubmitting || !(dirty && isValid) 
                        ? 'bg-zinc-400 cursor-not-allowed' 
                        : 'bg-emerald-600 hover:bg-emerald-500 rounded-md'
                    }`}
                    >
                    Register
                    </button>

                    </Form>
                )}
            </Formik>
    )
}

const Register = () => {

    const { customer } = useAuth()
    const navigate = useNavigate()

    useEffect(() => {
        if(customer){
            navigate("/dashboard")
        }
    })

    return (
        <>
        <div className="h-full" >
            <div className="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">
                <div className="sm:mx-auto sm:w-full sm:max-w-sm">
                <img
                    alt="Your Company"
                    src="/images/Logo.svg"
                    className="mx-auto h-10 w-auto"
                />
                <h2 className="mt-5 text-center text-2xl/9 font-bold tracking-tight text-white-900">
                    Register an account
                </h2>
                </div>

                <div className="mt-6 sm:mx-auto sm:w-full sm:max-w-sm">
                <RegisterationForm/>
                <p className="mt-10 text-center text-sm/6 text-white-500">
                    Already a member?{' '}
                    <a href="/" className="font-semibold text-emerald-600 hover:text-emerald-500">
                    Sign-in Now
                    </a>
                </p>
                </div>
            </div>
        </div>
        {/* Global notification live region, render this  manently at the end of the document */}
        {/* <div
        aria-live="assertive"
        className="pointer-events-none fixed inset-0 flex items-end px-4 py-6 sm:items-start sm:p-6"
        >
        <div className="flex w-full flex-col items-center space-y-4 sm:items-end">
                {notifications.map((notification) => (
                        <Notification 
                            key={notification.id}
                            text={notification.message}
                            type={notification.type}
                        />
                    ))}
        </div>
        </div> */}
        </>
    )
}

export default Register;